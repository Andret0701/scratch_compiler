package scratch_compiler.ValueFields;

import scratch_compiler.Field;

public class CeilField extends ValueField {
    public CeilField(ValueField input) {
        super("operator_mathop");
        setField(new Field("OPERATOR", "ceiling"));
        setInput("NUM", input);
    }
}