package scratch_compiler.ValueFields;

import scratch_compiler.Field;
import scratch_compiler.Function;
public class FunctionArgumentField extends ValueField {
    public FunctionArgumentField(Function function, String argument) {
        super("argument_reporter_string_number", function);
        setChildField(new Field("VALUE",argument));
    }
}
