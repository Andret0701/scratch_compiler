package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;

public class MoveBlock extends StackBlock {
    public MoveBlock(double steps) {
        this(new NumberField(steps));
    }

    public MoveBlock(ValueField steps) {
        super("motion_movesteps");
        setSteps(steps);
    }

    public void setSteps(ValueField steps) {
        setInput("STEPS", steps);
    }

}
