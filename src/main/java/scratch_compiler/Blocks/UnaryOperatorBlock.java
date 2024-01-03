package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.ValueField;

public class UnaryOperatorBlock extends ValueFieldBlock {
    private String inputName;
    public UnaryOperatorBlock(String opcode, String inputName)
    {
        super(opcode);
        this.inputName = inputName;
    }


    public void setInput(ValueField input) {
        setInput(inputName, input);
    }

}