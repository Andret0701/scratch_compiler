package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.VariableType;

public class IndexExpression extends Expression {

    public IndexExpression(Expression array, Expression index) {
        super(2);

        if (!array.getType().isArray())
            throw new IllegalArgumentException("The first expression must be an array");

        if (!index.getType().equals(new Type(VariableType.INT)))
            throw new IllegalArgumentException("The second expression must be an int");

        setExpression(0, array);
        setExpression(1, index);
    }

    public Expression getArray() {
        return getExpression(0);
    }

    public Expression getIndex() {
        return getExpression(1);
    }

    @Override
    public Type getType() {
        return new Type(getArray().getType().getType());
    }

    @Override
    public String toString() {
        return getArray().toString() + "[" + getIndex().toString() + "]";
    }
}
