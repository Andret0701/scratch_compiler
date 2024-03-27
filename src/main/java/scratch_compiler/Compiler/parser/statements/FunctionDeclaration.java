package scratch_compiler.Compiler.parser.statements;

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

    @Override
    public int getScopeCount() {
        return 1;
    }

    @Override
    public Scope getScope(int index) {
        if (index == 0)
            return scope;
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

    @Override
    public void setScope(int index, Scope scope) {
        if (index == 0)
            this.scope = scope;

        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

}