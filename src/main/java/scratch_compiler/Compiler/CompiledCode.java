package scratch_compiler.Compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.lexer.Lexer;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.optimiser.Optimizer;
import scratch_compiler.Compiler.parser.FunctionDeclarationParser;
import scratch_compiler.Compiler.parser.GlobalParser;
import scratch_compiler.Compiler.parser.StatementParser;
import scratch_compiler.Compiler.parser.StructDeclarationParser;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class CompiledCode {
    private Scope globalScope = new Scope();
    private ArrayList<FunctionDeclaration> functions = new ArrayList<>();
    private ArrayList<Type> structs = new ArrayList<>();

    public CompiledCode(Scope scope, ArrayList<FunctionDeclaration> functions, ArrayList<Type> structs) {
        this.globalScope = scope;
        this.functions = new ArrayList<>(functions);
        this.structs = new ArrayList<>(structs);
    }

    public static CompiledCode compile(String code, DeclarationTable declarationTable, boolean optimise) {
        ArrayList<Token> tokens = Lexer.lex(code);
        System.out.println(tokens);

        TokenReader reader = new TokenReader(tokens);
        CompiledCode compiledCode = GlobalParser.parse(reader, declarationTable);
        if (optimise)
            compiledCode = Optimizer.optimize(compiledCode);

        return compiledCode;
    }

    public ArrayList<FunctionDeclaration> getFunctions() {
        return new ArrayList<>(functions);
    }

    public void setFunctions(ArrayList<FunctionDeclaration> functions) {
        this.functions = new ArrayList<>(functions);
    }

    public ArrayList<Type> getStructs() {
        return new ArrayList<>(structs);
    }

    public void setStructs(ArrayList<Type> structs) {
        this.structs = new ArrayList<>(structs);
    }

    public Scope getGlobalScope() {
        return globalScope;
    }

    public void setGlobalScope(Scope globalScope) {
        this.globalScope = globalScope;
    }

    @Override
    public String toString() {
        String out = "";
        for (FunctionDeclaration function : functions) {
            out += function + "\n\n";
        }

        for (Statement statement : globalScope.getStatements()) {
            out += statement + "\n";
        }

        return out;
    }
}
