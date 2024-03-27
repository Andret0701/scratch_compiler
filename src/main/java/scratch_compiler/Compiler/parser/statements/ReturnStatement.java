package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class ReturnStatement extends Statement {
    public ReturnStatement(Expression expression) {
        super(1);
        setExpression(expression);
    }

    public Expression getExpression() {
        return getExpression(0);
    }

    public Type getType() {
        if (getExpression() == null)
            return new Type(VariableType.VOID);

        return getExpression().getType();
    }

    public void setExpression(Expression expression) {
        setExpression(0, expression);
    }

    @Override
    public String toString() {
        if (getExpression() == null)
            return "return";
        return "return " + getExpression();
    }
}