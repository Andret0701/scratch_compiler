package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.ScratchVariable;

public class VariableField extends ValueField{
    protected String name;
    protected boolean isGlobal;
    public VariableField(String name,boolean isGlobal) {
        super();
        this.name = name;
        this.isGlobal = isGlobal;
    }

    public ScratchVariable getVariable() {
        return new ScratchVariable(name,isGlobal);
    }

    @Override
    public ArrayList<ScratchVariable> getVariables() {
        ArrayList<ScratchVariable> variables = new ArrayList<ScratchVariable>();
        variables.add(getVariable());
        return variables;
    }
}
