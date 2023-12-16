package scratch_compiler;

public class ContainerBlock extends Block {
    protected Block child = null;

    public ContainerBlock(String opcode) {
        super(opcode);
    }

    public void setChild(Block block) {
        if (block == this || child == block)
            return;
        child = block;
    }

    @Override
    public String inputsToJSON() {
        if (child == null)
            return super.inputsToJSON();

        String json = "\"inputs\": {";
        json += "\"SUBSTACK\": [2, \"" + child.getId() + "\"]";
        json += "}";
        return json;
    }
}
