package scratch_compiler.Blocks;

import scratch_compiler.Field;
import scratch_compiler.Blocks.Types.StackBlock;

public class StopThisScriptBlock extends StackBlock {
    public StopThisScriptBlock() {
        super("control_stop");
        setField(new Field("STOP_OPTION", "this script"));
    }

    @Override
    public boolean isEnd() {
        return true;
    }
}