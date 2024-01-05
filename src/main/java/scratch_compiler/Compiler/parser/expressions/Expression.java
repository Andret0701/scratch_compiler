package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public abstract class Expression {
    public Expression() {
    }

    public abstract VariableType getType();

    public static void validateType(Expression expression, VariableType type, int line) {
        if (!expression.getType().canBeConvertedTo(type))
            throw new RuntimeException("Type mismatch at line " + line + " expected " + type + " got "
                    + expression.getType());
    }
}
