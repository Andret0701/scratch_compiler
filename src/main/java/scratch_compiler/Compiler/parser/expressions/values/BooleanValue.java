package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class BooleanValue extends Expression {
    private boolean value;

    public BooleanValue(boolean token) {
        super();
        this.value = token;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new Type(VariableType.BOOLEAN);
    }

    @Override
    public String toString() {
        return Boolean.toString(getValue());
    }

}
