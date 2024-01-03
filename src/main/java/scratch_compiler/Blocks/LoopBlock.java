package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.NumberField;

public class LoopBlock extends Block {
    public LoopBlock(NumberField count) {
        super("control_repeat");
        setLoopCount(count);
    }

    public LoopBlock(int count) {
        this(new NumberField(count));
    }

    public void connectInside(Block child) {
        connectChild(child,1);
    }

    public void setLoopCount(NumberField count) {
        setInput("TIMES", count);
    }

}
