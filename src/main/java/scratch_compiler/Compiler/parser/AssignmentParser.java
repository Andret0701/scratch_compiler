package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenSubtype;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.AdditionExpression;
import scratch_compiler.Compiler.parser.expressions.BinaryOperationExpression;
import scratch_compiler.Compiler.parser.expressions.DivisionExpression;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.IntValue;
import scratch_compiler.Compiler.parser.expressions.MultiplicationExpression;
import scratch_compiler.Compiler.parser.expressions.SubtractionExpression;
import scratch_compiler.Compiler.parser.expressions.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.Statement;

public class AssignmentParser {
    public static Assignment parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        Token token=tokens.expectNext(TokenType.IDENTIFIER);

        String name = token.getValue();
        identifierTypes.validateUsage(name, tokens.peek().getLine());

        TokenType operator=tokens.expectNext(TokenSubtype.ASSIGNMENT).getType();

        Expression value=null;
        if (operator!=TokenType.INCREMENT && operator!=TokenType.DECREMENT)
            value=ExpressionParser.parse(tokens, identifierTypes);

        VariableValue identifier = new VariableValue(name,identifierTypes.get(name));
        value = parseExpression(identifier, operator, value);

        if (!value.getType().canBeConvertedTo(identifier.getType()))
            throw new RuntimeException("Cannot convert " + value.getType() + " to " + identifier.getType());

        return new Assignment(name,value);
    }


    private static Expression parseExpression(VariableValue identifier, TokenType operator, Expression expression)
    {
        if(operator == TokenType.ASSIGN)
            return expression;

        BinaryOperationExpression binaryOperation;
        switch (operator){
            case INCREMENT:
                binaryOperation = ExpressionParser.getBinaryOperation(TokenType.ADD);
                expression = new IntValue(1);
                break;
            case DECREMENT:
                binaryOperation = ExpressionParser.getBinaryOperation(TokenType.SUB);
                expression = new IntValue(1);
                break;
            case ADD_ASSIGN:
                binaryOperation = ExpressionParser.getBinaryOperation(TokenType.ADD);
                break;
            case SUB_ASSIGN:
                binaryOperation = ExpressionParser.getBinaryOperation(TokenType.SUB);
                break;
            case MUL_ASSIGN:
                binaryOperation = ExpressionParser.getBinaryOperation(TokenType.MUL);
                break;
            case DIV_ASSIGN:
                binaryOperation=ExpressionParser.getBinaryOperation(TokenType.DIV);
                break;
            case MOD_ASSIGN:
                binaryOperation=ExpressionParser.getBinaryOperation(TokenType.MOD);
                break;
            default:
                throw new RuntimeException("Invalid operator " + operator);
        }

        binaryOperation.setLeft(identifier);
        binaryOperation.setRight(expression);
        return binaryOperation;
    }


}
