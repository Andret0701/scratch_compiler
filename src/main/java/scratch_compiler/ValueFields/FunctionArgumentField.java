package scratch_compiler.ValueFields;

import scratch_compiler.Field;
public class FunctionArgumentField extends ValueField {
    public FunctionArgumentField(String argument) {
        super("argument_reporter_string_number");
        setField(new Field("VALUE",argument));
    }
}
