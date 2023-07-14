package scratch_compiler.Variables;

import scratch_compiler.Utils;

public abstract class Variable {
    private String id;
    private String name;

    public Variable(String name) {
        this.id = Utils.generateID();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected abstract String valueToJSON();

    public String toJSON() {
        String json = "\"" + id + "\": [";
        json += "\"" + name + "\",";
        json += "\"" + valueToJSON() + "\"";
        json += "]";
        return json;
    }
}
