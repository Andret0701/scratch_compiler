package scratch_compiler.ValueFields.ScratchValues;

import java.util.ArrayList;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.ValueFieldBlock;
import scratch_compiler.ValueFields.ValueField;

public abstract class ScratchValueField extends ValueField {
    private ValueFieldBlock block;
    public ScratchValueField(String opcode) {
        super();
        block = new ValueFieldBlock(opcode);
    }


    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public ArrayList<Block> getBlocks(Block parent) {
        ArrayList<Block> blocks = new ArrayList<>();
        block.setParent(parent);
        blocks.add(block);
        return blocks;
    }

}