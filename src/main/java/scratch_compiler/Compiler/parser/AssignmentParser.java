package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;

public class AssignmentParser {
    public static Assignment parse(TokenReader tokens, DeclarationTable declarationTable) {
        Token token = tokens.peek();
        VariableValue variable = VariableParser.parse(tokens, declarationTable);
        if (variable.isArray() && variable.getIndex() == null)
            throw new RuntimeException("Cannot assign to array at line " + token.getLine());

        OperatorType operatorType = null;
        Expression value = null;

        TokenType operator = tokens.peek().getType();
        if (operator == TokenType.INCREMENT) {
            tokens.pop();
            operatorType = OperatorType.ADDITION;
            value = new IntValue(1);
        } else if (operator == TokenType.DECREMENT) {
            tokens.pop();
            operatorType = OperatorType.SUBTRACTION;
            value = new IntValue(1);
        } else {
            if (ExpressionParser.nextIsBinaryOperator(tokens))
                operatorType = ExpressionParser.getBinaryOperatorType(tokens.pop());

            tokens.expectNext(TokenType.ASSIGN);
            value = ExpressionParser.parse(variable.getType(), tokens, declarationTable);
        }

        value = parseExpression(variable, operatorType, value, declarationTable, token.getLine());

        // System.out.println("AssignmentParser: " + name + " = " + value);
        if (!variable.getType().equals(value.getType()))
            throw new RuntimeException("Cannot convert " + value.getType() + " to " + variable.getType() + " at line "
                    + token.getLine());

        return new Assignment(variable, value);
    }

    private static Expression parseExpression(VariableValue identifier, OperatorType operator, Expression expression,
            DeclarationTable declarationTable, int line) {
        if (operator == null)
            return expression;

        declarationTable.validateOperatorUsage(operator, identifier.getType(), expression.getType(), line);

        TypeDefinition returnType = declarationTable
                .getBinaryOperator(operator, identifier.getType().getType(), expression.getType().getType())
                .getReturnType();

        BinaryOperator binaryOperation = new BinaryOperator(operator, identifier, expression, new Type(returnType));
        return binaryOperation;
    }

}
