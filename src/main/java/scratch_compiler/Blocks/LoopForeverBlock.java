package scratch_compiler.Blocks;

public class LoopForeverBlock extends Block {

    public LoopForeverBlock() {
        super("control_forever");
    }

    public void connectInside(Block child) {
        connectChild(child, 1);
    }

    @Override
    public void connectUnder(Block child) {
        return;
    }

}
