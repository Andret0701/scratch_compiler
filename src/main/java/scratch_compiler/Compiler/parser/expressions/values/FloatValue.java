package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class FloatValue extends Expression {
    private double value;

    public FloatValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new Type(VariableType.FLOAT);
    }

    @Override
    public String toString() {
        return Double.toString(getValue());
    }

}
