package scratch_compiler.Blocks;

import scratch_compiler.Field;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.VariableField;

public class ClearListBlock extends StackBlock {
    private String name;
    private boolean isGlobal;
    public ClearListBlock(String name, boolean isGlobal) {
        super("data_deletealloflist");
        setVariable(name, isGlobal);
    }

        public void setVariable(String name, boolean isGlobal) {
        this.name = name;
        this.isGlobal = isGlobal;
        setField(new Field("LIST", new ScratchVariable(name, isGlobal, true)));
    }

    public void getVariable() {
        new VariableField(name, isGlobal);
    }
}
