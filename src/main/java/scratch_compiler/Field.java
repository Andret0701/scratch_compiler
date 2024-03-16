package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.ValueFields.ValueField;

public class Field {
    private String name;
    private String type;
    private ScratchVariable variable;
    public Field(String name, String type) {
        this.name = name;
        setType(type);
    }

    public Field(String name, ScratchVariable variable) {
        this.name = name;
        setVariable(variable);
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
        this.variable = null;
    }

    public void setVariable(ScratchVariable variable) {
        this.variable = variable;
        this.type = null;
    }


    public String getType() {
        return type;
    }

    public ScratchVariable getVariable() {
        return variable;
    }


}