package scratch_compiler.ValueFields;

import java.util.ArrayList;

import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.Blocks.BlockTypes.Interfaces.VariableHandler;
import scratch_compiler.Variables.Variable;

public class VariableField extends ValueField implements VariableHandler{
    protected String name;
    protected boolean isGlobal;
    public VariableField(String name,boolean isGlobal) {
        super();
        this.name = name;
        this.isGlobal = isGlobal;
    }

    @Override
    public Variable getVariable() {
        return new Variable(name,isGlobal);
    }

    @Override
    public Block getBlock() {
        return null;
    }

    @Override
    public ArrayList<Block> getBlocks() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Variable> getVariables() {
        ArrayList<Variable> variables = new ArrayList<Variable>();
        variables.add(getVariable());
        return variables;
    }
}
