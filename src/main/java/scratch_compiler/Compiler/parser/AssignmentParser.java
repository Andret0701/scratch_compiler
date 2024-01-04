package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.BinaryOperationNode;
import scratch_compiler.Compiler.parser.nodes.Expression;
import scratch_compiler.Compiler.parser.nodes.VariableNode;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.Statement;

public class AssignmentParser {
    public static Statement parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        if (!tokens.isNext(TokenType.IDENTIFIER))
            return null;

        String name = tokens.peek().getValue();
        if (!identifierTypes.contains(name))
            throw new RuntimeException("Identifier '" + name + "' not declared at line " + tokens.peek().getLine());
        tokens.next();

        TokenType operator = tokens.peek().getType();
        if (!isAssignmentOperator(operator))
            throw new RuntimeException("Expected assignment operator at line " + tokens.peek().getLine());


        tokens.next();
        Expression value = ExpressionParser.parse(tokens);
        if (value == null)
            throw new RuntimeException("Expected value at line " + tokens.peek().getLine());

        value = parseExpression(new VariableNode(name), operator, value);

        if (value.getType(identifierTypes) != identifierTypes.get(name))
            throw new RuntimeException("Type mismatch at line " + tokens.peek().getLine() + " expected "
                    + identifierTypes.get(name) + " got " + value.getType(identifierTypes));

        if (tokens.peek().getType() != TokenType.SEMICOLON)
            throw new RuntimeException("Expected ';' at line " + tokens.peek().getLine());
        tokens.next();

        return new Assignment(name,value);
    }

    private static boolean isAssignmentOperator(TokenType type) {
        return type == TokenType.ASSIGN || type == TokenType.ADD_ASSIGN || type == TokenType.SUB_ASSIGN
                || type == TokenType.MUL_ASSIGN || type == TokenType.DIV_ASSIGN;
    }

    private static Expression parseExpression(VariableNode identifier, TokenType operator, Expression expression)
    {
        if(operator == TokenType.ASSIGN)
            return expression;
        
        Token operatorToken;
        switch (operator){
            case ADD_ASSIGN:
                operatorToken = new Token(TokenType.PLUS, "+", -1);
                break;
            case SUB_ASSIGN:
                operatorToken = new Token(TokenType.MINUS, "-", -1);
                break;
            case MUL_ASSIGN:
                operatorToken = new Token(TokenType.MUL, "*", -1);
                break;
            case DIV_ASSIGN:
                operatorToken = new Token(TokenType.DIV, "/", -1);
                break;
            default:
                throw new RuntimeException("Invalid operator " + operator);
        }

        BinaryOperationNode binaryOperation = new BinaryOperationNode(operatorToken);
        binaryOperation.setLeft(identifier);
        binaryOperation.setRight(expression);
        return binaryOperation;
    }
}
