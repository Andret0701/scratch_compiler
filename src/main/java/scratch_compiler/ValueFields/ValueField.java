package scratch_compiler.ValueFields;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Field;
import scratch_compiler.Function;
import scratch_compiler.Variable;
import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.FieldBlock;

public abstract class ValueField {
    private FieldBlock block;
    private HashMap<String, ValueField> childInputs = new HashMap<>();
    private HashMap<String, Field> childFields = new HashMap<>();
    public ValueField(String opcode) {
        block = new FieldBlock(opcode);
    }

    public ValueField(String opcode, Function function) {
        block = new FieldBlock(opcode, function);
    }

    public ValueField() {
        block =null;
    }

    public Block getBlock() {
        return block;
    }

    protected void setChildInput(String fieldName, ValueField field) {
        childInputs.put(fieldName, field);
        block.setInput(fieldName, field);
    }

    protected void setChildField(Field field) {
        childFields.put(field.getName(), field);
        block.setField(field);
    }
    
    public ArrayList<Block> getBlocks(Block parent) {
        ArrayList<Block> blocks = new ArrayList<>();
        if (block==null)
            return blocks;

        block.setParent(parent);
        blocks.add(block);
        for (ValueField field : childInputs.values())
            blocks.addAll(field.getBlocks(block));
        for (Field field : childFields.values())
            blocks.addAll(field.getBlocks(block));
        return blocks;
    }

    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> variables = new ArrayList<>();
        for (ValueField field : childInputs.values())
            variables.addAll(field.getVariables());
        for (Field field : childFields.values())
            variables.addAll(field.getVariables());
        return variables;
    }
}
