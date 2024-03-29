package scratch_compiler.Compiler.parser;

import java.util.ArrayList;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.TypeField;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;

public class StructDeclarationParser {
    public static TypeDefinition parse(TokenReader tokens, DeclarationTable declarationTable) {
        tokens.expectNext(TokenType.STRUCT_DECLARATION);
        Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifierToken.getValue();
        tokens.expectNext(TokenType.OPEN_BRACE);
        ArrayList<TypeField> fields = parseFields(tokens, declarationTable);
        tokens.expectNext(TokenType.CLOSE_BRACE);

        TypeDefinition structType = new TypeDefinition(name, fields);
        declarationTable.declareType(structType);
        return structType;
    }

    public static boolean nextIsStructDeclaration(TokenReader tokens) {
        return tokens.peek().getType() == TokenType.STRUCT_DECLARATION;
    }

    private static ArrayList<TypeField> parseFields(TokenReader tokens, DeclarationTable declarationTable) {
        ArrayList<TypeField> fields = new ArrayList<>();
        while (TypeParser.nextIsType(tokens, declarationTable)) {
            TypeDefinition type = TypeParser.parseDefinition(tokens, declarationTable);
            Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
            String name = identifierToken.getValue();
            tokens.expectNext(TokenType.SEMICOLON);

            if (fields.stream().anyMatch(field -> field.getName().equals(name)))
                throw new RuntimeException(
                        "Cannot have two fields with the same name in a struct at line " + identifierToken.getLine());
            fields.add(new TypeField(name, type));
        }

        if (fields.isEmpty())
            throw new RuntimeException("Structs must have at least one field at line " + tokens.peek().getLine());
        return fields;
    }
}
