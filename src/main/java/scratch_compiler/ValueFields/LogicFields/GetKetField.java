package scratch_compiler.ValueFields.LogicFields;

import scratch_compiler.Field;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.ValueField;

public class GetKetField extends LogicField {
    public GetKetField(ValueField valueField) {
        super("sensing_keypressed");
        // if (valueField instanceof StringField)
        // setField(new Field("KEY_OPTION", ((StringField) valueField).getValue()));
        // else
        setInput("KEY_OPTION", valueField);
    }

    // public GetKetField(String key) {
    // super("sensing_keyoptions");
    // setField(new Field("KEY_OPTION", key));
    // }
}