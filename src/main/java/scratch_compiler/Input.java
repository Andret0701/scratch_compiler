package scratch_compiler;

import scratch_compiler.ValueFields.ValueField;

public class Input {
    private String name;
    private ValueField valueField;

    public Input(String name, ValueField valueField) {
        this.name = name;
        this.valueField = valueField;
    }

    public String getName() {
        return name;
    }

    public ValueField getValueField() {
        return valueField;
    }

    @Override
    public String toString() {
        return name + ": " + valueField;
    }
}
