package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class StatementParser {
    public static Statement parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        if (tokens.isAtEnd())
            return null;

        TokenType type = tokens.peek().getType();
        if (type==TokenType.OPEN_BRACE)
            return parseScope(tokens, identifierTypes);
        if (type==TokenType.IF)
            return IfParser.parse(tokens, identifierTypes);
        if (type==TokenType.WHILE)
            return WhileParser.parse(tokens, identifierTypes);
        if (type==TokenType.FOR)
           return ForParser.parse(tokens, identifierTypes);


        Statement statement=null;
        switch (type) {
            case INT_DECLARATION:
                statement= DeclarationParser.parse(tokens, identifierTypes);
                break;
            case FLOAT_DECLARATION:
                statement= DeclarationParser.parse(tokens, identifierTypes);
                break;
            case BOOLEAN_DECLARATION:
                statement= DeclarationParser.parse(tokens, identifierTypes);
                break;
            case STRING_DECLARATION:
                statement= DeclarationParser.parse(tokens, identifierTypes);
                break;
            case IDENTIFIER:
                statement= AssignmentParser.parse(tokens, identifierTypes);
                break;
            default:
                CompilerUtils.throwExpected("statement", tokens.peek().getLine(), tokens.peek());
        }
        
        tokens.expectNext(TokenType.SEMICOLON);
        return statement;
    }

    public static Statement parseScope(TokenReader tokens, IdentifierTypes identifierTypes) {
        if(!tokens.isNext(TokenType.OPEN_BRACE))
            return null;

        IdentifierTypes innerIdentifierTypes = identifierTypes.copy();
        Scope scope = new Scope(innerIdentifierTypes);

        tokens.next();
        while (!tokens.isAtEnd() && !tokens.isNext(TokenType.CLOSE_BRACE)) {
            Statement statement = StatementParser.parse(tokens, innerIdentifierTypes);
            if (statement == null)
                break;
            scope.addStatement(statement);
        }

        tokens.expectNext(TokenType.CLOSE_BRACE);
        return scope;
    }
}
