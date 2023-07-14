package scratch_compiler.ValueFields;

public class NumberField extends ValueField {
    protected double value;

    public NumberField(double value) {
        this.value = value;
    }

    public String toJSON() {
        return "[1,[4,\"" + value + "\"]]";
    }
}
