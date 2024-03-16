package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class StringValue extends Expression {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getString() {
        return value;
    }

    @Override
    public Type getType() {
        return new Type(VariableType.STRING);
    }

    @Override
    public String toString() {
        return "\"" + getString() + "\"";
    }
}
