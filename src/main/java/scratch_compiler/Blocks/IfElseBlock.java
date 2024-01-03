package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.LogicFields.LogicField;

public class IfElseBlock extends Block {
    public IfElseBlock(LogicField condition) {
        super("control_if_else");
        setCondition(condition);
    }

    public void setCondition(LogicField condition) {
        setInput("CONDITION", condition);
    }

       public void connectIf(Block child) {
        connectChild(child,1);
    }

    public void connectElse(Block child) {
        connectChild(child,2);
    }
}