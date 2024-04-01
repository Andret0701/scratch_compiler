package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.statements.Statement;

public class SimpleReturn extends Statement {
    public SimpleReturn() {
    }

    @Override
    public String toString() {
        return "return";
    }
}
