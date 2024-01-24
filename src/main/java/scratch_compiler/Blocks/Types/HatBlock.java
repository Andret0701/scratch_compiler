package scratch_compiler.Blocks.Types;

public class HatBlock extends Block{
    private BlockStack stack;
    public HatBlock(String opcode) {
        super(opcode);
        stack = new BlockStack();
    }

    public BlockStack getStack() {
        return stack;
    }

    public void push(StackBlock block) {
        stack.push(block);
    }

    public void push(BlockStack stack) {
        this.stack.push(stack);
    }
}

    