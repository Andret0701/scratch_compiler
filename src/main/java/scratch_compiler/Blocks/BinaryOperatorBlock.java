package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.ValueField;

public class BinaryOperatorBlock extends Block {
    private String leftInputName;
    private String rightInputName;
    public BinaryOperatorBlock(String opcode, String leftInputName, String rightInputName)
    {
        super(opcode);
        this.leftInputName = leftInputName;
        this.rightInputName = rightInputName;
    }

    public void setRight(ValueField right) {
        setInput(rightInputName, right);
    }

    public void setLeft(ValueField left) {
        setInput(leftInputName, left);
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }

}
