package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ArrayPop extends Statement {
    private String name;

    public ArrayPop(String name, Expression index) {
        super(1);
        this.name = name;
        setExpression(0, index);
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

    @Override
    public String toString() {
        return "pop " + name + "[" + getExpression(0) + "]";
    }

}
