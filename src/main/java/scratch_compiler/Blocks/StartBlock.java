package scratch_compiler.Blocks;

import scratch_compiler.Block;
import scratch_compiler.ContainerBlock;

public class StartBlock extends Block {
    public StartBlock() {
        super("event_whenflagclicked");
    }

    @Override
    public void connectTo(Block block) {
        return;
    }

    @Override
    public void connectInside(ContainerBlock block) {
        return;
    }
}
