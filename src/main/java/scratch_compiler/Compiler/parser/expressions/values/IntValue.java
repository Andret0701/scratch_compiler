package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class IntValue extends Expression {
    private int value;

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new Type(TypeDefinition.INT);
    }

    @Override
    public String toString() {
        return Integer.toString(getValue());
    }

    public boolean isConstant() {
        return true;
    }
}
