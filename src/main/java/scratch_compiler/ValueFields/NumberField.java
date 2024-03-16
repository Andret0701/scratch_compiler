package scratch_compiler.ValueFields;
public class NumberField extends ValueField {
    private String value;

    public NumberField(String value) {
        setValue(value);
    }

    public NumberField(double value) {
        setValue(value+"");
    }

    public NumberField(int value) {
        setValue(value+"");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value.length() == 0) {
            throw new IllegalArgumentException("Value cannot be empty");
        }
        //double regex
        if (!value.matches("-?[0-9]*\\.?[0-9]+")) {
            throw new IllegalArgumentException("Value must be a number");
        }
    }
}
