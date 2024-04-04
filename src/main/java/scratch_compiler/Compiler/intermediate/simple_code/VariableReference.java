package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class VariableReference extends Expression {
    private String name;
    private Type type;

    public VariableReference(String name, Type type, Expression index) {
        super(1);
        setExpression(0, index);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Expression getIndex() {
        return getExpression(0);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return ";" + name + "[" + getIndex() + "]";
    }
}
