package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
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
        return new Type(TypeDefinition.BOOL);
    }

    @Override
    public String toString() {
        return Boolean.toString(getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        BooleanValue rhs = (BooleanValue) obj;
        return value == rhs.value;
    }

    public boolean isConstant() {
        return true;
    }

}
