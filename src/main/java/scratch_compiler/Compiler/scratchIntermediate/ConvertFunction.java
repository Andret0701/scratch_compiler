package scratch_compiler.Compiler.scratchIntermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleReturn;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ConvertFunction {
    public static SimpleFunctionDeclaration convert(SimpleFunctionDeclaration function) {
        if (ConvertRecursiveFunction.isFunctionRecursive(function))
            return ConvertRecursiveFunction.convert(function);

        return new SimpleFunctionDeclaration(function.getName(),
                convertFunctionScope(ConvertScope.convert(function.getScope()), function));
    }

    private static Scope convertFunctionScope(Scope scope, SimpleFunctionDeclaration function) {
        Scope converted = new Scope();

        ArrayList<SimpleVariableDeclaration> variables = getDeclaredVariables(function.getScope());
        ArrayList<SimpleArrayDeclaration> arrays = getDeclaredArrays(function.getScope());

        for (Statement statement : scope.getStatements()) {
            converted.addAllStatements(convert(statement, function, variables, arrays));
        }
        return converted;
    }

    private static Scope convert(Scope scope, SimpleFunctionDeclaration function,
            ArrayList<SimpleVariableDeclaration> declaredVariables, ArrayList<SimpleArrayDeclaration> declaredArrays) {
        Scope converted = new Scope();
        for (Statement statement : scope.getStatements()) {
            converted.addAllStatements(convert(statement, function, declaredVariables, declaredArrays));
        }
        return converted;
    }

    private static ArrayList<Statement> convert(Statement statement, SimpleFunctionDeclaration function,
            ArrayList<SimpleVariableDeclaration> declaredVariables, ArrayList<SimpleArrayDeclaration> declaredArrays) {
        ArrayList<Statement> converted = new ArrayList<>();
        if (statement instanceof Scope)
            converted.add(convert((Scope) statement, function, declaredVariables, declaredArrays));
        else if (statement instanceof SimpleVariableDeclaration) {
            // remove the declaration
        } else if (statement instanceof SimpleArrayDeclaration) {
            // remove the declaration
        } else if (statement instanceof SimpleVariableAssignment) {
            SimpleVariableAssignment assignment = (SimpleVariableAssignment) statement;
            if (containsVariable(declaredVariables, assignment.getName())) {
                converted.add(new SimpleVariableAssignment(getVariableName(function, assignment.getName()),
                        convert(assignment.getValue(),
                                function, declaredVariables, declaredArrays)));
            } else
                converted.add(statement);

        } else if (statement instanceof SimpleArrayAssignment) {
            SimpleArrayAssignment assignment = (SimpleArrayAssignment) statement;
            if (containsArray(declaredArrays, assignment.getName())) {
                converted.add(new SimpleArrayAssignment(getVariableName(function, assignment.getName()),
                        convert(assignment.getValue(),
                                function, declaredVariables, declaredArrays),
                        convert(assignment.getIndex(),
                                function, declaredVariables, declaredArrays)));
            } else
                converted.add(statement);
        }

        else if (statement instanceof SimpleReturn) {
            converted.add(new SimpleReturn());
        } else {
            converted.add(statement);
            for (int i = 0; i < statement.getScopeCount(); i++) {
                statement.setScope(i, convert(statement.getScope(i), function, declaredVariables, declaredArrays));
            }
        }

        for (int i = 0; i < statement.getExpressionCount(); i++) {
            Expression expression = statement.getExpression(i);
            statement.setExpression(i, convert(expression, function, declaredVariables, declaredArrays));
        }

        return converted;
    }

    private static String getVariableName(SimpleFunctionDeclaration function, String variableName) {
        return "functionvar:" + function.getName() + ":" + variableName;
    }

    public static ArrayList<Statement> declareFunctionVariables(SimpleFunctionDeclaration function) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (SimpleArrayDeclaration array : getDeclaredArrays(function.getScope())) {
            IntValue size = new IntValue(200000);
            if (array.getSize() instanceof IntValue)
                size = (IntValue) array.getSize();

            statements.add(new SimpleArrayDeclaration(getVariableName(function,
                    array.getName()), array.getType(), size));
        }

        for (SimpleVariableDeclaration variable : getDeclaredVariables(function.getScope())) {
            statements.add(new SimpleVariableDeclaration(getVariableName(function, variable.getName()),
                    variable.getType()));
        }
        return statements;
    }

    private static ArrayList<SimpleVariableDeclaration> getDeclaredVariables(Scope scope) {
        ArrayList<SimpleVariableDeclaration> variables = new ArrayList<>();
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleVariableDeclaration)
                variables.add((SimpleVariableDeclaration) statement);

            if (statement instanceof Scope)
                variables.addAll(getDeclaredVariables((Scope) statement));
            else {
                for (int i = 0; i < statement.getScopeCount(); i++) {
                    variables.addAll(getDeclaredVariables(statement.getScope(i)));
                }
            }
        }
        return variables;
    }

    private static ArrayList<SimpleArrayDeclaration> getDeclaredArrays(Scope scope) {
        ArrayList<SimpleArrayDeclaration> variables = new ArrayList<>();
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleArrayDeclaration)
                variables.add((SimpleArrayDeclaration) statement);

            if (statement instanceof Scope)
                variables.addAll(getDeclaredArrays((Scope) statement));
            else {
                for (int i = 0; i < statement.getScopeCount(); i++) {
                    variables.addAll(getDeclaredArrays(statement.getScope(i)));
                }
            }
        }
        return variables;
    }

    private static boolean containsVariable(ArrayList<SimpleVariableDeclaration> variables, String variable) {
        for (SimpleVariableDeclaration var : variables) {
            if (var.getName().equals(variable))
                return true;
        }
        return false;
    }

    private static boolean containsArray(ArrayList<SimpleArrayDeclaration> arrays, String array) {
        for (SimpleArrayDeclaration var : arrays) {
            if (var.getName().equals(array))
                return true;
        }
        return false;
    }

    private static Expression convert(Expression expression, SimpleFunctionDeclaration function,
            ArrayList<SimpleVariableDeclaration> declaredVariables, ArrayList<SimpleArrayDeclaration> declaredArrays) {
        if (expression instanceof SimpleVariableValue) {
            SimpleVariableValue variable = (SimpleVariableValue) expression;
            if (containsVariable(declaredVariables, variable.getName())) {
                return new SimpleVariableValue(getVariableName(function, variable.getName()),
                        variable.getType().getType().getType());
            }
        }

        if (expression instanceof SimpleArrayValue) {
            SimpleArrayValue array = (SimpleArrayValue) expression;
            if (containsArray(declaredArrays, array.getName())) {
                return new SimpleArrayValue(getVariableName(function, array.getName()),
                        array.getType().getType().getType(), convert(array.getIndex(),
                                function, declaredVariables, declaredArrays));
            }
        }

        for (int i = 0; i < expression.getExpressionCount(); i++) {
            Expression child = expression.getExpression(i);
            expression.setExpression(i, convert(child, function, declaredVariables, declaredArrays));
        }

        return expression;
    }
}
