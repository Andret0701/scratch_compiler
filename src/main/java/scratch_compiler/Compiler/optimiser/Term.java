package scratch_compiler.Compiler.optimiser;

import scratch_compiler.Compiler.parser.expressions.Expression;

public class Term {
    private Expression expression;
    private boolean positive;
    public Term(Expression expression, boolean positive) {
        this.expression = expression;
        this.positive = positive;
    }

    public Expression getExpression() {
        return expression;
    }

    public boolean isPositive() {
        return positive;
    }

    @Override
    public String toString() {
        return (positive ? "+" : "-") + expression;
    }
}