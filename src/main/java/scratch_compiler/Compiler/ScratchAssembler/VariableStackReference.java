package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.ScratchVariable;


public class VariableStackReference {
    private ArrayList<ScratchVariable> variables = new ArrayList<ScratchVariable>();
    private ArrayList<Integer> scopeDepths = new ArrayList<Integer>();
    private int scopeDepth=0;

    public VariableStackReference() {
    }

    public void addScope() {
        scopeDepth++;
    }

    public int removeScope() {
        int removed = 0;
        while (scopeDepths.size()>0 && scopeDepths.get(scopeDepths.size()-1)==scopeDepth)
        {
            removeVariable();
            removed++;
        }
        scopeDepth--;
        return removed;
    }

    public void addVariable(ScratchVariable variable) {
        variables.add(variable);
        scopeDepths.add(scopeDepth);
    }

    public void removeVariable() {
        variables.remove(variables.size()-1);
        scopeDepths.remove(scopeDepths.size()-1);
    }

    public boolean isVariableDeclared(String name) {
        for (int i = variables.size()-1; i >= 0; i--) {
            if (variables.get(i).getName().equals(name))
                return true;
        }
        return false;
    }

    public int getVariableIndex(ScratchVariable variable) {
        return variables.indexOf(variable)+1;
    }
}
