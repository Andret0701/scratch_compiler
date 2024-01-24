package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.NumberField;

public class LoopBlock extends StackBlock {
    public LoopBlock(NumberField count) {
        super("control_repeat");
        setLoopCount(count);
    }

    public LoopBlock(int count) {
        this(new NumberField(count));
    }

    public void pushInside(StackBlock block) {
        push(0, block);
    }

    public void pushInside(BlockStack stack) {
        push(0, stack);
    }

    public void setLoopCount(NumberField count) {
        setInput("TIMES", count);
    }

}
