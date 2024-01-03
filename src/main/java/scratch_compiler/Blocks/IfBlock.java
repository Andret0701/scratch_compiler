package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.LogicFields.LogicField;

public class IfBlock extends Block {
    public IfBlock(LogicField condition) {
        super("control_if");
        setCondition(condition);
    }

    public void setCondition(LogicField condition) {
        setInput("CONDITION", condition);
    }

    public void connectIf(Block child) {
        connectChild(child,1);
    }
}