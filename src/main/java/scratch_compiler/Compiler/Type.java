package scratch_compiler.Compiler;

import scratch_compiler.Compiler.Types.BooleanType;
import scratch_compiler.Compiler.Types.NumberType;
import scratch_compiler.Compiler.Types.StringType;

public abstract class Type {
    protected Object value;

    public Type(Object value) {
        this.value = value;
    }

    public Type() {
        this.value = defaultValue();
    }

    public abstract String getType();

    public Object getValue() {
        return value;
    }

    public abstract Object defaultValue();
}
