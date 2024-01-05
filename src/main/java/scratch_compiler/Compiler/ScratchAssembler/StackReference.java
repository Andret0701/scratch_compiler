package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.Variable;


public class StackReference {
    private ArrayList<Variable> variables = new ArrayList<Variable>();
    private ArrayList<Integer> scopeDepths = new ArrayList<Integer>();
    private int scopeDepth=0;

    public StackReference() {
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

    public void addVariable(Variable variable) {
        variables.add(variable);
        scopeDepths.add(scopeDepth);
    }

    public void removeVariable() {
        variables.remove(variables.size()-1);
        scopeDepths.remove(scopeDepths.size()-1);
    }

    public int getVariableIndex(Variable variable) {
        return variables.indexOf(variable)+1;
    }
}
