package scratch_compiler.ValueFields;

public abstract class ValueField {
    public abstract String toJSON();

    public String blockDataToJSON(String parent_id) {
        return "";
    }

}
