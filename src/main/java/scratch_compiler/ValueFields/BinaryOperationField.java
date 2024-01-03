package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.Blocks.BinaryOperatorBlock;
import scratch_compiler.Blocks.Block;

public abstract class BinaryOperationField extends ValueField {
    private BinaryOperatorBlock block;

    private ValueField left;
    private ValueField right;
    public BinaryOperationField(String opcode, String leftInputName, String rightInputName, ValueField left, ValueField right) {
        block = new BinaryOperatorBlock(opcode, leftInputName, rightInputName);
        setLeft(left);
        setRight(right);
    }

    public void setLeft(ValueField left) {
        this.left = left;
        block.setLeft(left);
    }

    public void setRight(ValueField right) {
        this.right = right;
        block.setRight(right);
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
        if (left!=null)
            blocks.addAll(left.getBlocks(block));
        if (right!=null)
            blocks.addAll(right.getBlocks(block));
        return blocks;
    }
}
