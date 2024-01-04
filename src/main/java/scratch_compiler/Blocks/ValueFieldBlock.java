package scratch_compiler.Blocks;

public class ValueFieldBlock extends Block{
    public ValueFieldBlock(String opcode) {
        super(opcode);
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }


}
