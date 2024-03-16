package scratch_compiler.ValueFields;

import scratch_compiler.Field;
import scratch_compiler.ScratchVariable;

public class ListLengthField  extends ValueField {
    private String name;
    private boolean isGlobal;
    public ListLengthField(String name, boolean isGlobal) {
        super("data_lengthoflist");
        setVariable(name, isGlobal);
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
