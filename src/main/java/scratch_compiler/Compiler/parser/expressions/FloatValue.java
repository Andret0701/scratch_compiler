package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class FloatValue extends Expression {
    private double value;

    public FloatValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public VariableType getType() {
        return VariableType.FLOAT;
    }
    
    @Override
    public String toString() {
        return Double.toString(getValue());
    }

}
