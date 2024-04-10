package scratch_compiler.ValueFields;

import scratch_compiler.Field;

public class AbsField extends ValueField {
    public AbsField(ValueField input) {
        super("operator_mathop");
        setField(new Field("OPERATOR", "abs"));
        setInput("NUM", input);
    }
}