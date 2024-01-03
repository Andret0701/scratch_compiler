package scratch_compiler.ScratchObjects;

import java.util.ArrayList;

import scratch_compiler.Variable;
import scratch_compiler.Blocks.Block;

public abstract class ScratchObject {
    protected String name;
    private ArrayList<Block> blocks = new ArrayList<Block>();

    public ScratchObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean containsVariable(Variable variable) {
        return getVariables().contains(variable);
    }

    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> variables = new ArrayList<>();
        for (Block block : getBlocks()) {
            for (Variable variable : block.getVariables()) {
                if (!variables.contains(variable))
                    variables.add(variable);
            }
        }
        return variables;
    }

    public ArrayList<Variable> getGlobalVariables() {
        ArrayList<Variable> globalVariables = new ArrayList<>();
        for (Variable variable : getVariables()) {
            if (variable.isGlobal())
                globalVariables.add(variable);
        }
        return globalVariables;
    }

    public ArrayList<Variable> getLocalVariables() {
        ArrayList<Variable> localVariables = new ArrayList<>();
        for (Variable variable : getVariables()) {
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
