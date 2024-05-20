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

public class ConvertRecursiveFunction {
    public static SimpleFunctionDeclaration convert(SimpleFunctionDeclaration function) {
        return new SimpleFunctionDeclaration(function.getName(),
                convertFunctionScope(ConvertScope.convert(function.getScope()), function));
    }

    private static Scope convertFunctionScope(Scope scope, SimpleFunctionDeclaration function) {
        Scope converted = new Scope();
        // converted.addStatement(new SimpleVariableDeclaration(pointerName,
        // VariableType.INT));

        if (needsStackPointer(function)) {
            String pointerName = getPointerName(function);
            converted.addStatement(new SimpleVariableAssignment(pointerName, new BinaryOperator(OperatorType.ADDITION,
                    new SimpleVariableValue(pointerName, VariableType.INT), new IntValue(1),
                    new Type(VariableType.INT))));

        }
        ArrayList<Variable> declaredVariables = getDeclaredVariables(function.getScope());
        for (Statement statement : scope.getStatements()) {
            converted.addAllStatements(convert(statement, function, declaredVariables));
        }
        return converted;
    }

    private static Scope convert(Scope scope, SimpleFunctionDeclaration function,
            ArrayList<Variable> declaredVariables) {
        Scope converted = new Scope();
        for (Statement statement : scope.getStatements()) {
            converted.addAllStatements(convert(statement, function, declaredVariables));
        }
        return converted;
    }

    private static ArrayList<Statement> convert(Statement statement, SimpleFunctionDeclaration function,
            ArrayList<Variable> declaredVariables) {
        ArrayList<Statement> converted = new ArrayList<>();
        if (statement instanceof Scope)
            converted.add(convert((Scope) statement, function, declaredVariables));
        else if (statement instanceof SimpleVariableDeclaration) {
            // remove the declaration
        } else if (statement instanceof SimpleVariableAssignment) {
            SimpleVariableAssignment assignment = (SimpleVariableAssignment) statement;
            if (containsVariable(declaredVariables, assignment.getName())) {

                if (isFunctionRecursive(function)) {
                    converted.add(new SimpleArrayAssignment(getStackName(function, assignment.getName()),
                            new SimpleVariableValue(getPointerName(function), VariableType.INT),
                            convert(assignment.getValue(),
                                    function, declaredVariables)));
                } else {
                    converted.add(new SimpleVariableAssignment(getVariableName(function, assignment.getName()),
                            convert(assignment.getValue(),
                                    function, declaredVariables)));
                }
            } else {
                converted.add(statement);
            }
        } else if (statement instanceof SimpleReturn) {
            if (needsStackPointer(function)) {
                converted.add(new SimpleVariableAssignment(getPointerName(function),
                        new BinaryOperator(OperatorType.SUBTRACTION,
                                new SimpleVariableValue(
                                        getPointerName(function), VariableType.INT),
                                new IntValue(1), new Type(VariableType.INT))));
            }
            converted.add(new SimpleReturn());
        } else {
            converted.add(statement);
            for (int i = 0; i < statement.getScopeCount(); i++) {
                statement.setScope(i, convert(statement.getScope(i), function, declaredVariables));
            }
        }

        for (int i = 0; i < statement.getExpressionCount(); i++) {
            Expression expression = statement.getExpression(i);
            statement.setExpression(i, convert(expression, function, getDeclaredVariables(function.getScope())));
        }

        return converted;
    }

    private static String getPointerName(SimpleFunctionDeclaration function) {
        return "functionstack:pointer:" + function.getName();
    }

    private static String getStackName(SimpleFunctionDeclaration function, String variableName) {
        return "functionstack:stack:" + function.getName() + ":" + variableName;
    }

    private static String getArrayOffsetName(SimpleFunctionDeclaration function, String variableName) {
        return "functionstack:offset:" + function.getName() + ":" + variableName;
    }

    private static String getVariableName(SimpleFunctionDeclaration function, String variableName) {
        return "functionvar:" + function.getName() + ":" + variableName;
    }

    public static ArrayList<Statement> declareFunctionVariables(SimpleFunctionDeclaration function) {
        ArrayList<Statement> statements = new ArrayList<>();

        if (needsStackPointer(function)) {
            statements.add(new SimpleVariableDeclaration(getPointerName(function), VariableType.INT));
            statements.add(new SimpleVariableAssignment(getPointerName(function), new IntValue(0)));
        }

        for (Variable variable : getDeclaredVariables(function.getScope())) {
            if (!variable.getType().isArray())
                continue;
            IntValue size = new IntValue(200000);
            // check if the array is initialized with a constant value

            System.out.println("Variable: " + variable.getName());
            statements.add(new SimpleArrayDeclaration(getStackName(function,
                    variable.getName()),
                    variable.getType().getType().getType(),
                    size));

            // think this is only needed when function is recursive
            // statements.add(new SimpleArrayDeclaration(getArrayOffsetName(function,
            // variable.getName()),
            // variable.getType().getType().getType(),
            // new IntValue(200000)));
            // statements.add(new SimpleArrayAssignment(getArrayOffsetName(function,
            // variable.getName()),
            // new IntValue(0), new IntValue(0)));
        }

        if (isFunctionRecursive(function)) {
            for (Variable variable : getDeclaredVariables(function.getScope())) {
                if (!variable.getType().isArray())
                    statements.add(new SimpleArrayDeclaration(getStackName(function, variable.getName()),
                            variable.getType().getType().getType(),
                            new IntValue(200000)));
            }

            return statements;
        }

        for (Variable variable : getDeclaredVariables(function.getScope())) {
            if (!variable.getType().isArray())
                statements.add(new SimpleVariableDeclaration(getVariableName(function, variable.getName()),
                        variable.getType().getType().getType()));
            else {

                statements.add(new SimpleArrayDeclaration(getStackName(function, variable.getName()),
                        variable.getType().getType().getType(),
                        new IntValue(200000)));
            }
        }
        return statements;
    }

    private static boolean needsStackPointer(SimpleFunctionDeclaration function) {
        return isFunctionRecursive(function) || containsArray(function.getScope());
    }

    public static boolean isFunctionRecursive(SimpleFunctionDeclaration function) {
        return isFunctionRecursive(function.getScope(), function.getName());
    }

    private static boolean isFunctionRecursive(Scope scope, String functionName) {
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleFunctionCall) {
                SimpleFunctionCall functionCall = (SimpleFunctionCall) statement;
                if (functionCall.getName().equals(functionName))
                    return true;
            }

            if (statement instanceof Scope) {
                if (isFunctionRecursive((Scope) statement, functionName))
                    return true;
            }

            for (int i = 0; i < statement.getScopeCount(); i++) {
                if (isFunctionRecursive(statement.getScope(i), functionName))
                    return true;
            }
        }
        return false;
    }

    private static boolean containsArray(Scope scope) {
        ArrayList<Variable> variables = getDeclaredVariables(scope);
        for (Variable variable : variables) {
            if (variable.getType().isArray())
                return true;
        }
        return false;
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

    private static boolean containsVariable(ArrayList<Variable> variables, String variable) {
        for (Variable var : variables) {
            if (var.getName().equals(variable))
                return true;
        }
        return false;
    }

    private static Expression convert(Expression expression, SimpleFunctionDeclaration function,
            ArrayList<Variable> declaredVariables) {
        if (expression instanceof SimpleVariableValue) {
            SimpleVariableValue variable = (SimpleVariableValue) expression;
            if (containsVariable(declaredVariables, variable.getName())) {
                if (isFunctionRecursive(function)) {
                    return new SimpleArrayValue(getStackName(function, variable.getName()), VariableType.INT,
                            new SimpleVariableValue(getPointerName(function), variable.getType().getType().getType()));
                } else {
                    return new SimpleVariableValue(getVariableName(function, variable.getName()),
                            variable.getType().getType().getType());
                }
            }
        }

        for (int i = 0; i < expression.getExpressionCount(); i++) {
            Expression child = expression.getExpression(i);
            expression.setExpression(i, convert(child, function, declaredVariables));
        }

        return expression;
    }
}
