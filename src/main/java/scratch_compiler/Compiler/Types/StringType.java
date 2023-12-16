package scratch_compiler.Compiler.Types;

import scratch_compiler.Compiler.Type;

public class StringType extends Type {
    public String getType() {
        return "String";
    }

    public Object defaultValue() {
        return "";
    }
}
