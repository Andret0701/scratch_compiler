package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.LogicFields.LogicField;

public class IfBlock extends StackBlock {
    public IfBlock(LogicField condition) {
        super("control_if");
        setCondition(condition);
    }

    public void setCondition(LogicField condition) {
        setInput("CONDITION", condition);
    }

    public void pushIf(StackBlock block) {
        push(0, block);
    }

    public void pushIf(BlockStack stack) {
        push(0, stack);
    }
}