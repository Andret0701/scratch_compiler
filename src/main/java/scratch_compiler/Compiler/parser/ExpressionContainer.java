package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.parser.expressions.Expression;

public interface ExpressionContainer {
    public default Expression getExpression(int index) {
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

    public default int getExpressionCount() {
        return 0;
    }

    public default void setExpression(int index, Expression expression) {
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }
}
