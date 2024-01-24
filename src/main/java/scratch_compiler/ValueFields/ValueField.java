package scratch_compiler.ValueFields;


import scratch_compiler.Blocks.Types.Block;

public abstract class ValueField extends Block {
    public ValueField(String opcode) {
        super(opcode);
    }

    public ValueField()
    {
        super(null);
    }
}
