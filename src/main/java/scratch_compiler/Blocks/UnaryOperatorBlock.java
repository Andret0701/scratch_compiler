package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.ValueField;

public class UnaryOperatorBlock extends StackBlock {
    private String operandName;
    public UnaryOperatorBlock(String opcode, String operandName)
    {
        super(opcode);
        this.operandName = operandName;
    }


    public void setOperand(ValueField input) {
        setInput(operandName, input);
    }

}