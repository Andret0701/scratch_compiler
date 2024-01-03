package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.Variable;

public class VariableField extends ValueField{
    protected String name;
    protected boolean isGlobal;
    public VariableField(String name,boolean isGlobal) {
        super();
        this.name = name;
        this.isGlobal = isGlobal;
    }

    public Variable getVariable() {
        return new Variable(name,isGlobal);
    }

    @Override
    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> variables = new ArrayList<Variable>();
        variables.add(getVariable());
        return variables;
    }
}
