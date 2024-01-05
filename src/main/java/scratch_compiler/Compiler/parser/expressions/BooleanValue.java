package scratch_compiler.Compiler.parser.expressions;
import scratch_compiler.Compiler.parser.VariableType;

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
    public VariableType getType() {
        return VariableType.BOOLEAN;
    }

    @Override
    public String toString() {
        return Boolean.toString(getValue());
    }

}
