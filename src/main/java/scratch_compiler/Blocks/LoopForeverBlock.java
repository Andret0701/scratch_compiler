package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;

public class LoopForeverBlock extends StackBlock {

    public LoopForeverBlock() {
        super("control_forever");
    }

    public void pushInside(StackBlock block) {
        push(0, block);
    }

    public void pushInside(BlockStack stack) {
        push(0, stack);
    }

    @Override
    public boolean isEnd() {
        return true;
    }
}
