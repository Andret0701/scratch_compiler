package scratch_compiler.ValueFields;

import scratch_compiler.Field;
import scratch_compiler.ScratchVariable;

public class ListElementField extends UnaryOperationField {
    private String name;
    private boolean isGlobal;
    public ListElementField(String name, boolean isGlobal, ValueField index) {
        super("data_itemoflist", "INDEX", index);
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