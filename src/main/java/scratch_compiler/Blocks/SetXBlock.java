package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.ValueField;

public class SetXBlock extends UnaryOperatorBlock {
    public SetXBlock(ValueField input) {
        super("motion_setx", "X");
        setOperand(input);
    }
}