package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.VariableType;

public class SizeOfExpression extends Expression {
    public SizeOfExpression(Expression expression) {
        super(1);

        if (!expression.getType().isArray())
            throw new IllegalArgumentException("The expression must be an array");

        setExpression(0, expression);
    }

    public Expression getExpression() {
        return getExpression(0);
    }

    @Override
    public Type getType() {
        return new Type(VariableType.INT);
    }

    @Override
    public String toString() {
        return "sizeof(" + getExpression() + ")";
    }
}
