package scratch_compiler.ScratchObjects;

import java.util.ArrayList;

import scratch_compiler.Asset;
import scratch_compiler.Costume;
import scratch_compiler.ScratchFunction;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
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

    public boolean containsVariable(ScratchVariable variable) {
        return getVariables().contains(variable);
    }

    public boolean containsFunctionDefinition(String functionName) {
        for (HatBlock block : getBlocks()) {
            if (block instanceof FunctionDefinitionBlock) {
                ScratchFunction function = ((FunctionDefinitionBlock) block).getFunction();
                if (function.getName().equals(functionName))
                    return true;
            }
        }
        return false;
    }

    public ArrayList<ScratchVariable> getVariables() {
        ArrayList<ScratchVariable> variables = new ArrayList<>();
        for (Block block : getBlocks()) {
            for (ScratchVariable variable : block.getVariables()) {
                if (!variables.contains(variable))
                    variables.add(variable);
            }
        }
        return variables;
    }

    public ArrayList<ScratchVariable> getGlobalVariables() {
        ArrayList<ScratchVariable> globalVariables = new ArrayList<>();
        for (ScratchVariable variable : getVariables()) {
            if (variable.isGlobal())
                globalVariables.add(variable);
        }
        return globalVariables;
    }

    public ArrayList<ScratchVariable> getLocalVariables() {
        ArrayList<ScratchVariable> localVariables = new ArrayList<>();
        for (ScratchVariable variable : getVariables()) {
            if (!variable.isGlobal())
                localVariables.add(variable);
        }
        return localVariables;
    }

    public void addBlock(HatBlock block) {
        if (blocks.contains(block))
            throw new RuntimeException("ScratchObject already contains block");

        if (block instanceof FunctionDefinitionBlock) {
            ScratchFunction function = ((FunctionDefinitionBlock) block).getFunction();
            if (containsFunctionDefinition(function.getName()))
                throw new RuntimeException("ScratchObject already contains function definition");
        }

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
