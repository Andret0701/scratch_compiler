package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Variables.Variable;

public abstract class ValueField {
    public ValueField() {
        super();
    }   

    public Block getBlock() {
        return null;
    }

    public ArrayList<Block> getBlocks(Block parent) {
        return new ArrayList<Block>();
    }

    public ArrayList<Variable> getVariables() {
        return new ArrayList<Variable>();
    }
}
