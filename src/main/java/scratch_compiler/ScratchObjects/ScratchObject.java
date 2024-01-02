package scratch_compiler.ScratchObjects;

import java.util.ArrayList;

import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.Variables.Variable;

public abstract class ScratchObject {
    protected String name;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Variable> variables = new ArrayList<Variable>();

    public ScratchObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }



    public void addVariable(Variable variable) {
        if (containsVariable(variable.getName()))
            throw new IllegalArgumentException("Variable with name " + variable.getName() + " already exists");
        variables.add(variable);
    }

    public boolean containsVariable(String name) {
        for (Variable variable : variables) {
            if (variable.getName().equals(name))
                return true;
        }
        return false;
    }

    public ArrayList<Variable> getVariables() {
        return new ArrayList<>(variables);
    }

    public ArrayList<Variable> getGlobalVariables() {
        ArrayList<Variable> globalVariables = new ArrayList<>();
        for (Variable variable : variables) {
            if (variable.isGlobal())
                globalVariables.add(variable);
        }
        return globalVariables;
    }

    public ArrayList<Variable> getLocalVariables() {
        ArrayList<Variable> localVariables = new ArrayList<>();
        for (Variable variable : variables) {
            if (!variable.isGlobal())
                localVariables.add(variable);
        }
        return localVariables;
    }

    public void addBlock(Block block) {
        Block clone = block.clone();
        blocks.add(clone);
    }

    public ArrayList<Block> getBlocks() {
        return new ArrayList<>(blocks);
    }
}
