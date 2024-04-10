package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Statement;

public class SimpleArrayAssignment extends Statement {
    private String name;

    public SimpleArrayAssignment(String name, Expression index, Expression value) {
        super(2);
        this.name = name;
        setExpression(0, index);
        setExpression(1, value);
    }

    public String getName() {
        return name;
    }

    public Expression getIndex() {
        return getExpression(0);
    }

    public void setIndex(Expression index) {
        setExpression(0, index);
    }

    public Expression getValue() {
        return getExpression(1);
    }

    @Override
    public String toString() {
        return name + "[" + getIndex() + "] = " + getValue();
    }
}
