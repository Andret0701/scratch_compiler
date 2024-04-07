package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;

public class SetPositionBlock extends BinaryOperatorBlock {
    public SetPositionBlock(ValueField x, ValueField y) {
        super("motion_gotoxy", "X", "Y");
        setLeft(x);
        setRight(y);
    }

    public SetPositionBlock(double x, double y) {
        this(new NumberField(x), new NumberField(y));
    }
}