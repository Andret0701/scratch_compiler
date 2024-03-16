package scratch_compiler.Compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.lexer.Lexer;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.optimiser.Optimiser;
import scratch_compiler.Compiler.parser.FunctionDeclarationParser;
import scratch_compiler.Compiler.parser.StatementParser;
import scratch_compiler.Compiler.parser.StructDeclarationParser;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class CompiledCode {
    private ArrayList<FunctionDeclaration> functions = new ArrayList<>();

    public CompiledCode(ArrayList<FunctionDeclaration> functions) {
        this.functions = new ArrayList<>(functions);
    }

    public static CompiledCode compile(String code, DeclarationTable declarationTable) {
        ArrayList<Token> tokens = Lexer.lex(code);
        System.out.println(tokens);

        TokenReader reader = new TokenReader(tokens);

        ArrayList<FunctionDeclaration> functions = new ArrayList<>();
        while (!reader.isAtEnd()) {
            if (FunctionDeclarationParser.nextIsFunctionDeclaration(reader, declarationTable))
                functions.add(FunctionDeclarationParser.parse(reader, declarationTable));
            else if (StructDeclarationParser.nextIsStructDeclaration(reader))
                StructDeclarationParser.parse(reader, declarationTable);
            else
                throw new RuntimeException("Unexpected token " + reader.peek());
        }

        // return Optimiser.optimise(new CompiledCode(functions));
        return new CompiledCode(functions);
    }

    public ArrayList<FunctionDeclaration> getFunctions() {
        return new ArrayList<>(functions);
    }

    @Override
    public String toString() {
        String out = "";
        for (FunctionDeclaration function : functions) {
            out += function + "\n";
        }
        return out;
    }
}
