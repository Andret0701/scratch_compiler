package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class IntValue extends Expression {
    private int value;

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public VariableType getType() {
        return VariableType.INT;
    }

    @Override
    public String toString() {
        return Integer.toString(getValue());
    }
}
