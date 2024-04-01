package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.statements.Statement;

public class SimpleFunctionCall extends Statement {
    private String name;

    public SimpleFunctionCall(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "call " + name;
    }
}
