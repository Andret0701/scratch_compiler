package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class StringValue extends Expression {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public VariableType getType() {
        return VariableType.STRING;
    }

    @Override
    public String toString() {
        return getValue();
    }
}

