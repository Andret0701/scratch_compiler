package scratch_compiler.ScratchObjects;

import java.util.ArrayList;

import scratch_compiler.Asset;
import scratch_compiler.Costume;
import scratch_compiler.Variable;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.HatBlock;

public abstract class ScratchObject {
    protected String name;
    private ArrayList<HatBlock> blocks = new ArrayList<HatBlock>();
    private ArrayList<Costume> costumes = new ArrayList<Costume>();

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

    public void addBlock(HatBlock block) {
        if (blocks.contains(block))
            throw new RuntimeException("ScratchObject already contains block");

        blocks.add(block);
    }

    public ArrayList<HatBlock> getBlocks() {
        return new ArrayList<>(blocks);
    }

    public void addCostume(Costume costume) {
        Costume clone = costume.clone();
        costumes.add(clone);
    }

    public ArrayList<Costume> getCostumes() {
        return new ArrayList<>(costumes);
    }

    public ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<>();
        assets.addAll(getCostumes());
        return assets;
    }

}
