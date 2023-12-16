package scratch_compiler.Compiler.Types;

import scratch_compiler.Compiler.Type;

public class NumberType extends Type {
    public String getType() {
        return "Number";
    }

    public Object defaultValue() {
        return 0;
    }
}
