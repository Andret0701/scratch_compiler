package scratch_compiler.Blocks.Types;

import java.util.ArrayList;
public class StackBlock extends Block {
    private boolean end;
    private ArrayList<BlockStack> substacks = new ArrayList<BlockStack>();
    public StackBlock(String opcode, Boolean end) {
        super(opcode);
        this.end = end;
    }

    public StackBlock(String opcode) {
        super(opcode);
        end = false;
    }

    public boolean isEnd() {
        return end;
    }

    protected void push(int stackIndex, StackBlock block) {
        if(stackIndex<0)
            throw new IllegalArgumentException("Stack index must be greater than or equal to 0");
        while (substacks.size() <= stackIndex)
            substacks.add(new BlockStack());

        substacks.get(stackIndex).push(block);
    }

    protected void push(int stackIndex, BlockStack stack) {
        if(stackIndex<0)
            throw new IllegalArgumentException("Stack index must be greater than or equal to 0");
        while (substacks.size() <= stackIndex)
            substacks.add(new BlockStack());

        substacks.get(stackIndex).push(stack);
    }

    public ArrayList<BlockStack> getSubstacks() {
        return new ArrayList<BlockStack>(substacks);
    }

}