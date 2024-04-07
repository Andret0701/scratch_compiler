package scratch_compiler.ValueFields;

import scratch_compiler.Field;

public class SinField extends ValueField {
    public SinField(ValueField value) {
        super("operator_mathop");
        setField(new Field("OPERATOR", "sin"));
        setInput("NUM", value);
    }
}