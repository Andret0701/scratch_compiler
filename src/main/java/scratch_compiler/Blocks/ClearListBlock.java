package scratch_compiler.Blocks;

import scratch_compiler.Field;
import scratch_compiler.Variable;
import scratch_compiler.ValueFields.VariableField;

public class ClearListBlock extends Block {
    private String name;
    private boolean isGlobal;
    public ClearListBlock(String name, boolean isGlobal) {
        super("data_deletealloflist");
        setVariable(name, isGlobal);
    }

        public void setVariable(String name, boolean isGlobal) {
        this.name = name;
        this.isGlobal = isGlobal;
        setField(new Field("LIST", new Variable(name, isGlobal, true)));
    }

    public void getVariable() {
        new VariableField(name, isGlobal);
    }
}
