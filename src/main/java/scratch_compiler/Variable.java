package scratch_compiler;

public class Variable {
    private String id;
    private String name;
    private String value;

    public Variable(String name, String value) {
        this.id = Utils.generateID();
        this.name = name;
        this.value = value;
    }

    public String toJSON() {
        String json = "\"" + id + "\": [";
        json += "\"" + name + "\",";
        json += "\"" + value + "\"";
        json += "]";
        return json;
    }

    public String getId() {
        return id;
    }
}
