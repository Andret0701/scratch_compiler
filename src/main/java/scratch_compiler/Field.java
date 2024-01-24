package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.ValueFields.ValueField;

public class Field {
    private String name;
    private String type;
    private Variable variable;
    public Field(String name, String type) {
        this.name = name;
        setType(type);
    }

    public Field(String name, Variable variable) {
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

    public void setVariable(Variable variable) {
        this.variable = variable;
        this.type = null;
    }


    public String getType() {
        return type;
    }

    public Variable getVariable() {
        return variable;
    }


}