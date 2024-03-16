package scratch_compiler.Blocks;

import scratch_compiler.Field;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;

public class ChangeVariableBlock  extends StackBlock{
    private String name;
    private boolean isGlobal;
    public ChangeVariableBlock(String name, boolean isGlobal, ValueField value) {
        super("data_changevariableby");
        setVariable(name, isGlobal);
        setValue(value);
    }

    public void setVariable(String name, boolean isGlobal) {
        this.name = name;
        this.isGlobal = isGlobal;
        setField(new Field("VARIABLE", new ScratchVariable(name, isGlobal)));
    }

    public void getVariable() {
        new VariableField(name, isGlobal);
    }

    public void setValue(ValueField value) {
        setInput("VALUE", value);
    }
}
