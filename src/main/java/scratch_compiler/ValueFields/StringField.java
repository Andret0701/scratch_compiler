package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.Blocks.BlockTypes.Block;

public class StringField extends ValueField {
    private String value;
    public StringField(String value) {
        super();
        this.value = value;
    }   

    public String getValue() {
        return value;
    }

    public void setValue(String newValue) {
        value = newValue;
    }

    @Override
    public Block getBlock() {
        return null;
    }

    @Override
    public ArrayList<Block> getBlocks() {
        return new ArrayList<>();
    }
}
