package scratch_compiler.Compiler.parser;

import javax.swing.plaf.nimbus.State;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.Expression;
import scratch_compiler.Compiler.parser.statements.Declaration;
import scratch_compiler.Compiler.parser.statements.Statement;

public class DeclarationParser {
    public static Statement parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        if (!tokens.isNext(TokenType.INT_DECLARATION) && !tokens.isNext(TokenType.FLOAT_DECLARATION)
                && !tokens.isNext(TokenType.BOOLEAN_DECLARATION))
            return null;

        switch (tokens.peek().getType()) {
            case INT_DECLARATION:
                return parseVariableDeclaration(tokens, identifierTypes, TokenType.INT);
            case FLOAT_DECLARATION:
                return parseVariableDeclaration(tokens, identifierTypes, TokenType.FLOAT);
            case BOOLEAN_DECLARATION:
                return parseVariableDeclaration(tokens, identifierTypes, TokenType.BOOLEAN);
            default:
                CompilerUtils.throwExpected("declaration", tokens.peek().getLine(), tokens.peek());
        }
        throw new RuntimeException("Unreachable");
    }

    private static Statement parseVariableDeclaration(TokenReader tokens, IdentifierTypes identifierTypes,
            TokenType type) {
        tokens.next();
        if (tokens.peek().getType() != TokenType.IDENTIFIER)
            CompilerUtils.throwExpected("identifier", tokens.peek().getLine(), tokens.peek());

        String name = tokens.peek().getValue();
        if (identifierTypes.contains(name))
            throw new RuntimeException("Identifier '" + name + "' already declared at line "
                    + tokens.peek().getLine());
        tokens.next();

        if (tokens.peek().getType() != TokenType.ASSIGN)
            CompilerUtils.throwExpected("=", tokens.peek().getLine(), tokens.peek());
        tokens.next();

        Expression expression = ExpressionParser.parse(tokens);
        if (!validAssignmentType(type, expression.getType(identifierTypes)))
            throw new RuntimeException("Type mismatch at line " + tokens.peek().getLine() + " expected " + type
                    + " got " + expression.getType(identifierTypes));

        if (tokens.peek().getType() != TokenType.SEMICOLON)
            CompilerUtils.throwExpected(";", tokens.peek().getLine(), tokens.peek());
        tokens.next();

        identifierTypes.add(name, type);
        return new Declaration(name,type, expression);
    }

    private static boolean validAssignmentType(TokenType target, TokenType source) {
        if (target==source)
            return true;
        if (target == TokenType.FLOAT && source == TokenType.INT)
            return true;
        return false;
    }

}
