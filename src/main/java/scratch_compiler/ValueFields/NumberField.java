package scratch_compiler.ValueFields;
public class NumberField extends ValueField {
    private double value;

    public NumberField(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double newValue) {
        value = newValue;
    }
}
