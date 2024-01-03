package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.UnaryOperatorBlock;

public class UnaryOperationField extends ValueField {
    private UnaryOperatorBlock block;

    private ValueField input;
    public UnaryOperationField(String opcode, String inputName, ValueField input) {
        block = new UnaryOperatorBlock(opcode, inputName);
        setInput(input);
    }

    public void setInput(ValueField input) {
        this.input = input;
        block.setInput(input);
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
        if (input!=null)
            blocks.addAll(input.getBlocks(block));
        return blocks;
    }
}

