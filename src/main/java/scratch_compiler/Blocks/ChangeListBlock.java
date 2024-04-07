package scratch_compiler.Blocks;

import scratch_compiler.Field;
import scratch_compiler.ScratchVariable;
import scratch_compiler.ValueFields.ValueField;

public class ChangeListBlock extends BinaryOperatorBlock {
    private String name;
    private boolean isGlobal;

    public ChangeListBlock(String name, boolean isGlobal, ValueField index, ValueField input) {
        super("data_replaceitemoflist", "INDEX", "ITEM");
        setVariable(name, isGlobal);
        setLeft(index);
        setRight(input);
    }

    public void setVariable(String name, boolean isGlobal) {
        this.name = name;
        this.isGlobal = isGlobal;
        setField(new Field("LIST", new ScratchVariable(name, isGlobal, true)));
    }

    public ScratchVariable getVariable() {
        return new ScratchVariable(name, isGlobal, true);
    }
}
