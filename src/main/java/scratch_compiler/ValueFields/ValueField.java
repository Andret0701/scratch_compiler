package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.Variables.Variable;

public abstract class ValueField {
    public ValueField() {
        super();
    }   

    public Block getBlock() {
        return null;
    }
    
    public ArrayList<Block> getBlocks() {
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(getBlock());
        return blocks;
    }

    public ArrayList<Variable> getVariables() {
        return new ArrayList<Variable>();
    }
}
