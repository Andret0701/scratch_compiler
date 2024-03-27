package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class GlobalParser {
    public static CompiledCode parse(TokenReader tokens, DeclarationTable declarationTable) {
        ArrayList<FunctionDeclaration> functions = new ArrayList<>();
        ArrayList<TypeDefinition> structs = new ArrayList<>();
        Scope globalScope = new Scope();

        while (!tokens.isAtEnd()) {
            if (FunctionDeclarationParser.nextIsFunctionDeclaration(tokens, declarationTable))
                functions.add(FunctionDeclarationParser.parse(tokens, declarationTable));
            else if (StructDeclarationParser.nextIsStructDeclaration(tokens))
                structs.add(StructDeclarationParser.parse(tokens, declarationTable));
            else {
                Statement statement = StatementParser.parse(tokens, declarationTable);
                if (statement != null)
                    globalScope.addStatement(statement);
                else
                    throw new RuntimeException("Unexpected token " + tokens.peek());
            }

        }

        return new CompiledCode(globalScope, functions, structs);
    }
}
