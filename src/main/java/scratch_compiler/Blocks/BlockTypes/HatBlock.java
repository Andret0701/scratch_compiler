package scratch_compiler.Blocks.BlockTypes;

public class HatBlock extends Block {
    public HatBlock(String opcode) {
        super(opcode);
    }

    @Override //make parent method do nothing
    public void connectTo(Block parent) {
        return;
    }

    @Override //make parent method do nothing
    public void connectInside(ContainerBlock block) {
        return;
    }
    

}