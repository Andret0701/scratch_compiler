package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;
import scratch_compiler.Compiler.parser.statements.Statement;

public class DeclarationParser {
    public static VariableDeclaration parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        switch (tokens.peek().getType()) {
            case INT_DECLARATION:
                return parseVariableDeclaration(tokens, identifierTypes, TokenType.INT_DECLARATION,VariableType.INT);
            case FLOAT_DECLARATION:
                return parseVariableDeclaration(tokens, identifierTypes, TokenType.FLOAT_DECLARATION,VariableType.FLOAT);
            case BOOLEAN_DECLARATION:
                return parseVariableDeclaration(tokens, identifierTypes, TokenType.BOOLEAN_DECLARATION,VariableType.BOOLEAN);
            case STRING_DECLARATION:
                return parseVariableDeclaration(tokens, identifierTypes, TokenType.STRING_DECLARATION,VariableType.STRING);
            default:
                CompilerUtils.throwExpected("declaration", tokens.peek().getLine(), tokens.peek());
        }
        throw new RuntimeException("Unreachable");
    }

    private static VariableDeclaration parseVariableDeclaration(TokenReader tokens, IdentifierTypes identifierTypes, TokenType declarationType,VariableType type) {
        tokens.expectNext(declarationType);

        if (tokens.isNext(TokenType.SQUARE_BRACKET_OPEN)) 
            return parseArrayDeclaration(tokens, identifierTypes, declarationType,type);

        Token identifier=tokens.expectNext(TokenType.IDENTIFIER);

        String name = identifier.getValue();
        identifierTypes.validateDeclaration(name, identifier.getLine());

        if (tokens.peek().getType()==TokenType.SEMICOLON) 
        {
            tokens.expectNext(TokenType.SEMICOLON);
            identifierTypes.add(name, type);
            return new VariableDeclaration(name,type, ExpressionParser.getDefaultValue(type));
        }

        tokens.expectNext(TokenType.ASSIGN);

        Expression expression = ExpressionParser.parse(tokens, identifierTypes);
        if (!expression.getType().canBeConvertedTo(type))
            throw new RuntimeException("Cannot convert " + expression.getType() + " to " + type);


        identifierTypes.add(name, type);
        return new VariableDeclaration(name,type, expression);
    }

    private static VariableDeclaration parseArrayDeclaration(TokenReader tokens, IdentifierTypes identifierTypes, TokenType declarationType,VariableType type) {
        
        tokens.expectNext(TokenType.SQUARE_BRACKET_OPEN);
        tokens.expectNext(TokenType.SQUARE_BRACKET_CLOSE);
        
        Token identifier=tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifier.getValue();
        identifierTypes.validateDeclaration(name, identifier.getLine());

        tokens.expectNext(TokenType.ASSIGN);
        tokens.expectNext(declarationType);
        tokens.expectNext(TokenType.SQUARE_BRACKET_OPEN);

        Expression expression = ExpressionParser.parse(tokens, identifierTypes);
        Expression.validateType(expression, VariableType.INT, tokens.peek().getLine());
        

        tokens.expectNext(TokenType.SQUARE_BRACKET_CLOSE);
        tokens.expectNext(TokenType.SEMICOLON);

        identifierTypes.add(name, type);
        return new VariableDeclaration(name,type, expression);
    }



}
