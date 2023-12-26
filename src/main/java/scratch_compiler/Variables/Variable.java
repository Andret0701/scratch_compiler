package scratch_compiler.Variables;

import scratch_compiler.Utils;
import scratch_compiler.JSON.ArrayJSON;

public abstract class Variable extends ArrayJSON {
    private VariableType type;
    private boolean isGlobal;

    public Variable(String name,VariableType type,boolean isGlobal) {
        this.type = type;
        this.isGlobal=isGlobal;
        
        add(name);
        add("0");
    }


    public String getName() {
        return (String)get(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable))
            return false;
        
        Variable other = (Variable) obj;
        return this.getName().equals(other.getName());
    }
}
