package scratch_compiler.Blocks;

import scratch_compiler.Field;
import scratch_compiler.Function;
import scratch_compiler.ValueFields.ValueField;

public class FieldBlock extends Block {
    public FieldBlock(String opcode) {
        super(opcode);
    }

    public FieldBlock(String opcode, Function function) {
        super(opcode, function);
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }

    public void setInput(String inputName, ValueField input) {
        super.setInput(inputName, input);
    }

    public void setField(Field field) {
        super.setField(field);
    }
    
    public void connectUnder(Block parent) {
        return;
    }
}
