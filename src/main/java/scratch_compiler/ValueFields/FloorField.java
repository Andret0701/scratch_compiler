package scratch_compiler.ValueFields;

import scratch_compiler.Field;

public class FloorField extends ValueField {
    public FloorField(ValueField input) {
        super("operator_mathop");
        setField(new Field("OPERATOR", "floor"));
        setInput("NUM", input);
    }
}