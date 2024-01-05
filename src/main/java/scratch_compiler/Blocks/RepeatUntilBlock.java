package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.LogicFields.LogicField;

public class RepeatUntilBlock extends Block {
    public RepeatUntilBlock(LogicField condition) {
        super("control_repeat_until");
        setCondition(condition);
    }

    public void setCondition(LogicField condition) {
        setInput("CONDITION", condition);
    }

    public void connectRepeat(Block child) {
        connectChild(child,1);
    }
}