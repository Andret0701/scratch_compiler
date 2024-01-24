package scratch_compiler.Blocks;

import scratch_compiler.Field;
import scratch_compiler.Variable;
import scratch_compiler.ValueFields.ValueField;
public class AddListBlock extends UnaryOperatorBlock {
    private String name;
    private boolean isGlobal;
    public AddListBlock(String name, boolean isGlobal, ValueField input) {
        super("data_addtolist", "ITEM");
        setVariable(name, isGlobal);
        setOperand(input);
    }

    public void setVariable(String name, boolean isGlobal) {
        this.name = name;
        this.isGlobal = isGlobal;
        setField(new Field("LIST", new Variable(name, isGlobal, true)));
    }

    public Variable getVariable() {
        return new Variable(name, isGlobal);
    }
}