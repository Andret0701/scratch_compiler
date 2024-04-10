package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
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

public class IntermediateCode {
    private Scope globalScope = new Scope();
    private ArrayList<SimpleFunctionDeclaration> functions = new ArrayList<>();
    private boolean zeroIndexed = true;

    public IntermediateCode(Scope globalScope, ArrayList<SimpleFunctionDeclaration> functions) {
        this.globalScope = globalScope;
        this.functions = new ArrayList<>(functions);
    }

    public ArrayList<SimpleFunctionDeclaration> getFunctions() {
        return new ArrayList<>(functions);
    }

    public void setFunctions(ArrayList<SimpleFunctionDeclaration> functions) {
        this.functions = new ArrayList<>(functions);
    }

    public Scope getGlobalScope() {
        return globalScope;
    }

    public void setGlobalScope(Scope globalScope) {
        this.globalScope = globalScope;
    }

    public boolean isZeroIndexed() {
        return zeroIndexed;
    }

    public void setZeroIndexed(boolean zeroIndexed) {
        this.zeroIndexed = zeroIndexed;
    }

    @Override
    public String toString() {
        String out = "";
        for (SimpleFunctionDeclaration function : functions) {
            out += function + "\n\n";
        }

        for (Statement statement : globalScope.getStatements()) {
            out += statement + "\n";
        }

        return out;
    }
}
