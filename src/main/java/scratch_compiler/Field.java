package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.Blocks.Block;
import scratch_compiler.ValueFields.ValueField;

public class Field {
    private String name;
    private String type;
    private Variable variable;
    private ValueField valueField;

    public Field(String name, String type) {
        this.name = name;
        setType(type);
    }

    public Field(String name, Variable variable) {
        this.name = name;
        setVariable(variable);
    }

    public Field(String name, ValueField valueField) {
        this.name = name;
        setValueField(valueField);
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
        this.variable = null;
        this.valueField = null;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
        this.type = null;
        this.valueField = null;
    }

    public void setValueField(ValueField valueField) {
        this.valueField = valueField;
        this.type = null;
        this.variable = null;
    }

    public String getType() {
        return type;
    }

    public Variable getVariable() {
        return variable;
    }

    public ValueField getValueField() {
        return valueField;
    }

    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> variables = new ArrayList<Variable>();
        if (variable != null) 
            variables.add(variable);
         else if (valueField != null) 
            variables.addAll(valueField.getVariables());
    
        return variables;
    }

    public ArrayList<Block> getBlocks(Block parent) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        if (valueField != null) 
            blocks.addAll(valueField.getBlocks(parent));
        return blocks;
    }

}