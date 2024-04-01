package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.statements.Statement;

public class Pop extends Statement {
    private String name;

    public Pop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "pop " + name;
    }

}