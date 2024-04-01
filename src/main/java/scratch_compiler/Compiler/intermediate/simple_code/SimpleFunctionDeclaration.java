package scratch_compiler.Compiler.intermediate.simple_code;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class SimpleFunctionDeclaration extends Statement {
    private String name;
    private Scope scope;

    public SimpleFunctionDeclaration(String name, Scope scope) {
        this.name = name;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return name + " -> " + scope.toString();
    }
}