package scratch_compiler.ValueFields;

import scratch_compiler.Field;
import scratch_compiler.Variable;

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
        setField(new Field("LIST", new Variable(name, isGlobal, true)));
    }

    public Variable getVariable() {
        return new Variable(name, isGlobal, true);
    }


    
}
