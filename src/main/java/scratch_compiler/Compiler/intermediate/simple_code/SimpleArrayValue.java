package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class SimpleArrayValue extends Expression {
    private String name;
    private VariableType type;

    public SimpleArrayValue(String name, VariableType type, Expression index) {
        super(1);
        this.name = name;
        this.type = type;
        setExpression(0, index);
    }

    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return new Type(type);
    }

    public Expression getIndex() {
        return getExpression(0);
    }

    @Override
    public String toString() {
        return name + "[" + getIndex() + "]";
    }

}
