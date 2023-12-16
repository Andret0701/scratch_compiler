package scratch_compiler.Compiler.Types;

import scratch_compiler.Compiler.Type;

public class BooleanType extends Type {
    public String getType() {
        return "Boolean";
    }

    public Object defaultValue() {
        return false;
    }
}
