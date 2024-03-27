package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.SizeOfExpression;
import scratch_compiler.Compiler.parser.expressions.types.Associativity;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.ErrorStatement;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ConvertStatement {
    public static ArrayList<Statement> convert(Statement statement) {
        ArrayList<Statement> statements = new ArrayList<>();
        statements.addAll(getIndexCheck(statement));
        if (statement instanceof VariableDeclaration)
            statements.addAll(ConvertDeclaration.convert((VariableDeclaration) statement));
        else
            statements.add(statement);

        for (Statement converted : statements) {
            ConvertExpression.convert(converted);
        }

        return statements;
    }

    private static ArrayList<Statement> getIndexCheck(Statement statement) {
        ArrayList<VariableValue> indexed = ConvertExpression.getIndexed(statement);
        ArrayList<Statement> statements = new ArrayList<>();

        if (indexed.size() == 0)
            return statements;

        Expression check = getIndexCheck(indexed.get(0));
        for (int i = 1; i < indexed.size(); i++) {
            check = new BinaryOperator(OperatorType.OR, check, getIndexCheck(indexed.get(i)),
                    new TypeDefinition(VariableType.BOOLEAN));
        }

        ErrorStatement error = new ErrorStatement("Index out of bounds");
        IfStatement ifStatement = new IfStatement(check, error);
        statements.add(ifStatement);
        return statements;
    }

    private static Expression getIndexCheck(VariableValue variableValue) {
        Expression index = variableValue.getIndex();
        BinaryOperator bottomCheck = new BinaryOperator(OperatorType.GREATER_THAN, new IntValue(0),
                index,
                new TypeDefinition(VariableType.BOOLEAN));
        BinaryOperator topCheck = new BinaryOperator(OperatorType.LESS_EQUALS,
                index,
                ConvertExpression.convertSize(new SizeOfExpression(variableValue)),
                new TypeDefinition(VariableType.BOOLEAN));
        return new BinaryOperator(OperatorType.OR, bottomCheck, topCheck, new TypeDefinition(VariableType.BOOLEAN));
    }
}
