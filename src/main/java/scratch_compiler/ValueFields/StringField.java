package scratch_compiler.ValueFields;
public class StringField extends ValueField {
    private String value;
    public StringField(String value) {
        super();
        this.value = value;
    }   

    public String getValue() {
        return value;
    }

    public void setValue(String newValue) {
        value = newValue;
    }
}
