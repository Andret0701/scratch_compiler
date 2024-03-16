package scratch_compiler.Blocks.Types;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Field;
import scratch_compiler.Input;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ScratchVariable;

public abstract class Block {
    protected String opcode;
    protected HashMap<String, Input> inputs = new HashMap<>();
    protected HashMap<String, Field> fields = new HashMap<>();

    private int count = 0;
    public Block(String opcode) {
        this.opcode = opcode;

        count = blockCount;
        blockCount++;
    }

    public String getOpcode() {
        return opcode;
    }

    protected void setInput(String name, ValueField field) {
        Input input = new Input(name, field);
        inputs.put(name, input);
    }

    public ArrayList<Input> getInputs() {
        return new ArrayList<Input>(inputs.values());
    }

    protected void setField(Field field) {
        fields.put(field.getName(), field);
    }

    public ArrayList<Field> getFields() {
        return new ArrayList<Field>(fields.values());
    }

    public ArrayList<ScratchVariable> getVariables() {
        ArrayList<ScratchVariable> variables = new ArrayList<ScratchVariable>();
        for (Input input : getInputs()) {
            ArrayList<ScratchVariable> inputVariables = input.getValueField().getVariables();
            variables.addAll(inputVariables);
        }
        for (Field field : getFields()) {
            ScratchVariable variable = field.getVariable();
            if (variable != null)
                variables.add(variable);
        }
        return variables;
    }

    @Override
    public String toString() {
        String string = opcode;
        return string;
    }

    static int blockCount = 0;
    @Override
    public int hashCode() {
        return count;
    }

}
