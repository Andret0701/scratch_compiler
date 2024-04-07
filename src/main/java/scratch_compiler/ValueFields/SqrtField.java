package scratch_compiler.ValueFields;

import scratch_compiler.Field;

public class SqrtField extends ValueField {
    public SqrtField(ValueField value) {
        super("operator_mathop");
        setField(new Field("OPERATOR", "sqrt"));
        setInput("NUM", value);
    }
}