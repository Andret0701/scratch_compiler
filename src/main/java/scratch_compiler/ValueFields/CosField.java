package scratch_compiler.ValueFields;

import scratch_compiler.Field;

public class CosField extends ValueField {
    public CosField(ValueField value) {
        super("operator_mathop");
        setField(new Field("OPERATOR", "cos"));
        setInput("NUM", value);
    }
}
