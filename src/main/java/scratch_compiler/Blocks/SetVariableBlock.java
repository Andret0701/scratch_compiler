package scratch_compiler.Blocks;

import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;

public class SetVariableBlock extends UnaryOperatorBlock{
    private String name;
    private boolean isGlobal;
    public SetVariableBlock(String name, boolean isGlobal, ValueField input) {
        super("data_setvariableto", "VALUE");
        setVariable(name, isGlobal);
        setInput(input);
    }

    public void setVariable(String name, boolean isGlobal) {
        this.name = name;
        this.isGlobal = isGlobal;
        setField("VARIABLE", new VariableField(name, isGlobal));
    }

    public void getVariable() {
        new VariableField(name, isGlobal);
    }

}