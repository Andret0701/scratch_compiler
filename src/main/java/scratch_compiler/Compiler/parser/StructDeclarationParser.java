package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;

public class StructDeclarationParser {
    public static void parse(TokenReader tokens, DeclarationTable declarationTable) {
        tokens.expectNext(TokenType.STRUCT_DECLARATION);
        Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifierToken.getValue();
        tokens.expectNext(TokenType.OPEN_BRACE);

        ArrayList<Variable> fields = new ArrayList<>();
        while (tokens.peek().getType() != TokenType.CLOSE_BRACE)
            fields.add(parseVariable(tokens, declarationTable));
        tokens.expectNext(TokenType.CLOSE_BRACE);

        Type structType = new Type(name, fields);
        declarationTable.declareType(structType);
    }

    public static boolean nextIsStructDeclaration(TokenReader tokens) {
        return tokens.peek().getType() == TokenType.STRUCT_DECLARATION;
    }

    public static Variable parseVariable(TokenReader tokens, DeclarationTable declarationTable) {
        Type type = TypeParser.parse(tokens, declarationTable);
        Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifierToken.getValue();
        tokens.expectNext(TokenType.SEMICOLON);
        return new Variable(name, type);
    }
}
