package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.ValueField;

public class SetYBlock extends UnaryOperatorBlock {
    public SetYBlock(ValueField input) {
        super("motion_sety", "Y");
        setOperand(input);
    }
}