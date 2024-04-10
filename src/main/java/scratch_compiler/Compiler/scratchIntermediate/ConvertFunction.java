package scratch_compiler.Compiler.scratchIntermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
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

public class ConvertFunction {
    public static SimpleFunctionDeclaration convert(SimpleFunctionDeclaration function) {
        return new SimpleFunctionDeclaration(function.getName(),
                convertFunctionScope(ConvertScope.convert(function.getScope()), function));
    }

    private static Scope convertFunctionScope(Scope scope, SimpleFunctionDeclaration function) {
        Scope converted = new Scope();
        String pointerName = "functionstack:pointer:" + function.getName();
        // converted.addStatement(new SimpleVariableDeclaration(pointerName,
        // VariableType.INT));
        converted.addStatement(new SimpleVariableAssignment(pointerName, new BinaryOperator(OperatorType.ADDITION,
                new SimpleVariableValue(pointerName, VariableType.INT), new IntValue(1), new Type(VariableType.INT))));

        ArrayList<String> declaredVariables = getDeclaredVariables(function.getScope());
        for (Statement statement : scope.getStatements()) {
            converted.addAllStatements(convert(statement, function, declaredVariables));
        }
        return converted;
    }

    private static Scope convert(Scope scope, SimpleFunctionDeclaration function, ArrayList<String> declaredVariables) {
        Scope converted = new Scope();
        for (Statement statement : scope.getStatements()) {
            converted.addAllStatements(convert(statement, function, declaredVariables));
        }
        return converted;
    }

    private static ArrayList<Statement> convert(Statement statement, SimpleFunctionDeclaration function,
            ArrayList<String> declaredVariables) {
        ArrayList<Statement> converted = new ArrayList<>();
        if (statement instanceof Scope)
            converted.add(convert((Scope) statement, function, declaredVariables));
        else if (statement instanceof SimpleReturn) {
            converted.add(new SimpleVariableAssignment(getPointerName(function),
                    new BinaryOperator(OperatorType.SUBTRACTION,
                            new SimpleVariableValue(
                                    getPointerName(function), VariableType.INT),
                            new IntValue(1), new Type(VariableType.INT))));
            converted.add(new SimpleReturn());
        } else if (statement instanceof SimpleVariableDeclaration) {
            // remove the declaration
        } else if (statement instanceof SimpleVariableAssignment) {
            SimpleVariableAssignment assignment = (SimpleVariableAssignment) statement;
            if (declaredVariables.contains(assignment.getName())) {
                converted.add(new SimpleArrayAssignment(getStackName(function, assignment.getName()),
                        new SimpleVariableValue(getPointerName(function), VariableType.INT),
                        convert(assignment.getValue(),
                                function, declaredVariables)));
            } else {
                converted.add(statement);
            }
        } else if (statement instanceof SimpleReturn) {
            converted.add(new SimpleVariableAssignment(getPointerName(function),
                    new BinaryOperator(OperatorType.SUBTRACTION,
                            new SimpleVariableValue(
                                    getPointerName(function), VariableType.INT),
                            new IntValue(1), new Type(VariableType.INT))));
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

    public static ArrayList<Statement> declareFunctionStacks(SimpleFunctionDeclaration function) {
        ArrayList<Statement> statements = new ArrayList<>();
        statements.add(new SimpleVariableDeclaration(getPointerName(function), VariableType.INT));
        statements.add(new SimpleVariableAssignment(getPointerName(function), new IntValue(0)));
        for (String variable : getDeclaredVariables(function.getScope())) {
            statements.add(new SimpleArrayDeclaration(getStackName(function, variable), VariableType.INT,
                    new IntValue(200000)));
        }
        return statements;
    }

    private static ArrayList<String> getDeclaredVariables(Scope scope) {
        ArrayList<String> variables = new ArrayList<>();
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleVariableDeclaration) {
                SimpleVariableDeclaration declaration = (SimpleVariableDeclaration) statement;
                variables.add(declaration.getName());
            }

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

    private static Expression convert(Expression expression, SimpleFunctionDeclaration function,
            ArrayList<String> declaredVariables) {
        if (expression instanceof SimpleVariableValue) {
            SimpleVariableValue variable = (SimpleVariableValue) expression;
            if (declaredVariables.contains(variable.getName())) {
                return new SimpleArrayValue(getStackName(function, variable.getName()), VariableType.INT,
                        new SimpleVariableValue(getPointerName(function), VariableType.INT));
            }
        }

        for (int i = 0; i < expression.getExpressionCount(); i++) {
            Expression child = expression.getExpression(i);
            expression.setExpression(i, convert(child, function, declaredVariables));
        }

        return expression;
    }
}
