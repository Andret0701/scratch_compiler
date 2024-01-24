package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.LogicFields.LogicField;

public class IfElseBlock extends StackBlock {
    public IfElseBlock(LogicField condition) {
        super("control_if_else");
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

    public void pushElse(StackBlock block) {
        push(1, block);
    }

    public void pushElse(BlockStack stack) {
        push(1, stack);
    }
}