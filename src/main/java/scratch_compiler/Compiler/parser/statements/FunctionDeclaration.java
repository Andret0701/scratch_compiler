package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.Function;

public class FunctionDeclaration extends Statement {
    private Function function;
    private Scope scope;

    public FunctionDeclaration(Function function, Scope scope) {
        this.function = function;
        this.scope = scope;
    }

    public Function getFunction() {
        return function;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return function.toString() + " " + scope.toString();
    }

    public ArrayList<Statement> getChildren() {
        return new ArrayList<Statement>();
    }
}