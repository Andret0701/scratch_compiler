package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
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
        return new Type(TypeDefinition.FLOAT);
    }

    @Override
    public String toString() {
        return Double.toString(getValue());
    }

    public boolean isConstant() {
        return true;
    }

}
