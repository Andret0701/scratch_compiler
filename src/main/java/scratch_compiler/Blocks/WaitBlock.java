package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;

public class WaitBlock extends StackBlock {

    public WaitBlock(ValueField duration) {
        super("control_wait");
        setDuration(duration);
    }

    public WaitBlock(double duration) {
        this(new NumberField(duration));
    }


    public void setDuration(ValueField duration) {
        setInput("DURATION", duration);
    }


}
