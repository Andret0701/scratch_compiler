package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.LogicFields.LogicField;

public class RepeatUntilBlock extends StackBlock {
    public RepeatUntilBlock(LogicField condition) {
        super("control_repeat_until");
        setCondition(condition);
    }

    public void setCondition(LogicField condition) {
        setInput("CONDITION", condition);
    }

    public void pushRepeat(StackBlock block) {
        push(0, block);
    }

    public void pushRepeat(BlockStack stack) {
        push(0, stack);
    }
}