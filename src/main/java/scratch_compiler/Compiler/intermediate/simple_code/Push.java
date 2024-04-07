package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Statement;

public class Push extends Statement {
    public Push(Expression expression) {
        super(1);
        setExpression(0, expression);
    }

    public Expression getExpression() {
        return getExpression(0);
    }

    @Override
    public String toString() {
        return "push " + getExpression(0);
    }
}
