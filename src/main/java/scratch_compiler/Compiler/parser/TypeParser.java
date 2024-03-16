package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;

public class TypeParser {
    public static Type parse(TokenReader tokens, DeclarationTable declarationTable) {
        Token token = tokens.pop();
        String typeName = token.getValue();
        if (!declarationTable.isTypeDeclared(typeName))
            CompilerUtils.throwExpected("type", token.getLine(), token);
        return declarationTable.getType(typeName);
    }

    public static boolean nextIsType(TokenReader tokens, DeclarationTable declarationTable) {
        return declarationTable.isTypeDeclared(tokens.peek().getValue());
    }
}
