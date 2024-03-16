package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;

public abstract class Expression {
    public Expression() {
    }

    public abstract Type getType();
}
