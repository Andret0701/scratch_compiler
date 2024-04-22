package scratch_compiler.Compiler.optimiser;

import scratch_compiler.Compiler.intermediate.simple_code.ArrayPop;
import scratch_compiler.Compiler.intermediate.simple_code.Pop;
import scratch_compiler.Compiler.intermediate.simple_code.Push;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleReturn;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.SystemCallExpression;
import scratch_compiler.Compiler.parser.expressions.TypeConversionExpression;
import scratch_compiler.Compiler.parser.expressions.UnaryOperator;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StringValue;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.SystemCallStatement;
import scratch_compiler.Compiler.parser.statements.WhileStatement;

public class CopyCode {
    public static Scope copy(Scope scope) {
        if (scope == null)
            return null;

        Scope copy = new Scope();
        for (Statement statement : scope.getStatements()) {
            if (statement != null)
                copy.addStatement(copy(statement));
        }
        return copy;
    }

    private static Statement copy(Statement statement) {
        if (statement instanceof Scope) {
            return copy((Scope) statement);
        } else if (statement instanceof SimpleArrayAssignment) {
            SimpleArrayAssignment arrayAssignment = (SimpleArrayAssignment) statement;
            statement = new SimpleArrayAssignment(arrayAssignment.getName(), arrayAssignment.getIndex(),
                    arrayAssignment.getValue());
        } else if (statement instanceof SimpleArrayDeclaration) {
            SimpleArrayDeclaration arrayDeclaration = (SimpleArrayDeclaration) statement;
            statement = new SimpleArrayDeclaration(arrayDeclaration.getName(), arrayDeclaration.getType(),
                    arrayDeclaration.getSize());
        } else if (statement instanceof SimpleVariableAssignment) {
            SimpleVariableAssignment variableAssignment = (SimpleVariableAssignment) statement;
            statement = new SimpleVariableAssignment(variableAssignment.getName(), variableAssignment.getValue());
        } else if (statement instanceof SimpleVariableDeclaration) {
            SimpleVariableDeclaration variableDeclaration = (SimpleVariableDeclaration) statement;
            statement = new SimpleVariableDeclaration(variableDeclaration.getName(), variableDeclaration.getType());
        } else if (statement instanceof IfStatement) {
            IfStatement ifStatement = (IfStatement) statement;
            if (ifStatement.getElseScope() != null)
                statement = new IfStatement(ifStatement.getExpression(), copy(ifStatement.getIfScope()),
                        copy(ifStatement.getElseScope()));
            else
                statement = new IfStatement(ifStatement.getExpression(), copy(ifStatement.getIfScope()));
        } else if (statement instanceof WhileStatement) {
            WhileStatement whileStatement = (WhileStatement) statement;
            statement = new WhileStatement(whileStatement.getExpression(), copy(whileStatement.getScope()));
        } else if (statement instanceof Pop) {
            Pop pop = (Pop) statement;
            statement = new Pop(pop.getName());
        } else if (statement instanceof ArrayPop) {
            ArrayPop arrayPop = (ArrayPop) statement;
            statement = new ArrayPop(arrayPop.getName(), arrayPop.getIndex());
        } else if (statement instanceof Push) {
            Push push = (Push) statement;
            statement = new Push(push.getExpression());
        } else if (statement instanceof SimpleReturn) {
            statement = new SimpleReturn();
        } else if (statement instanceof SystemCallStatement) {
            SystemCallStatement systemCallStatement = (SystemCallStatement) statement;
            statement = new SystemCallStatement(systemCallStatement.getSystemCall(),
                    systemCallStatement.getArguments());
        } else if (statement instanceof SimpleFunctionCall) {
            SimpleFunctionCall simpleFunctionCall = (SimpleFunctionCall) statement;
            statement = new SimpleFunctionCall(simpleFunctionCall.getName());
        } else {
            throw new IllegalArgumentException("Unknown statement type: " + statement.getClass());
        }

        for (int i = 0; i < statement.getScopeCount(); i++) {
            Scope scope = statement.getScope(i);
            statement.setScope(i, copy(scope));
        }
        copy((ExpressionContainer) statement);
        return statement;
    }

    private static void copy(ExpressionContainer container) {
        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);
            expression = copy(expression);
            copy((ExpressionContainer) expression);
            container.setExpression(i, expression);
        }
    }

    private static Expression copy(Expression expression) {
        if (expression instanceof SimpleArrayValue) {
            SimpleArrayValue arrayValue = (SimpleArrayValue) expression;
            expression = new SimpleArrayValue(arrayValue.getName(), arrayValue.getType().getType().getType(),
                    arrayValue.getIndex());
        } else if (expression instanceof SimpleVariableValue) {
            SimpleVariableValue variableValue = (SimpleVariableValue) expression;
            expression = new SimpleVariableValue(variableValue.getName(), variableValue.getType().getType().getType());
        } else if (expression instanceof BinaryOperator) {
            BinaryOperator binaryOperator = (BinaryOperator) expression;
            expression = new BinaryOperator(binaryOperator.getOperatorType(), binaryOperator.getLeft(),
                    binaryOperator.getRight(), binaryOperator.getType());
        } else if (expression instanceof UnaryOperator) {
            UnaryOperator unaryOperator = (UnaryOperator) expression;
            expression = new UnaryOperator(unaryOperator.getOperatorType(), unaryOperator.getOperand(),
                    unaryOperator.getType());
        } else if (expression instanceof SystemCallExpression) {
            SystemCallExpression systemCallExpression = (SystemCallExpression) expression;
            expression = new SystemCallExpression(systemCallExpression.getSystemCall(),
                    systemCallExpression.getArguments());
        } else if (expression instanceof TypeConversionExpression) {
            TypeConversionExpression typeConversionExpression = (TypeConversionExpression) expression;
            expression = new TypeConversionExpression(typeConversionExpression.getExpression(),
                    typeConversionExpression.getType());
        } else if (expression instanceof IntValue) {
            IntValue intValue = (IntValue) expression;
            expression = new IntValue(intValue.getValue());
        } else if (expression instanceof FloatValue) {
            FloatValue floatValue = (FloatValue) expression;
            expression = new FloatValue(floatValue.getValue());
        } else if (expression instanceof BooleanValue) {
            BooleanValue booleanValue = (BooleanValue) expression;
            expression = new BooleanValue(booleanValue.getValue());
        } else if (expression instanceof StringValue) {
            StringValue stringValue = (StringValue) expression;
            expression = new StringValue(stringValue.getString());
        } else
            throw new IllegalArgumentException("Unknown expression type: " + expression.getClass());
        return expression;

    }
}