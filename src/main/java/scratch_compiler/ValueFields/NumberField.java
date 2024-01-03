package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.Blocks.Block;

public class NumberField extends ValueField {
    private double value;

    public NumberField(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double newValue) {
        value = newValue;
    }

    @Override
    public Block getBlock() {
        return null;
    }

    @Override
    public ArrayList<Block> getBlocks(Block parent) {
        return new ArrayList<>();
    }
}
