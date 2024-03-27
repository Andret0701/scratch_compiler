package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;

public class Assignment extends Statement {

    public Assignment(VariableValue variable, Expression expression) {
        super(2);
        setExpression(0, variable);
        setExpression(1, expression);
    }

    public VariableValue getVariable() {
        return (VariableValue) getExpression(0);
    }

    public Expression getExpression() {
        return getExpression(1);
    }

    @Override
    public String toString() {
        return getVariable() + " = " + getExpression();
    }
}
