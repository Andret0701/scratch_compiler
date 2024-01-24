package scratch_compiler.Compiler;

import scratch_compiler.Compiler.parser.VariableType;

public class CompiledVariable {
    private String name;
    private VariableType type;
    private boolean readOnly;
    private boolean canBeRedecleared;

    public CompiledVariable(String name, VariableType type, boolean readOnly, boolean canBeRedecleared) {
        this.name = name;
        this.type = type;
        this.readOnly = readOnly;
        this.canBeRedecleared = canBeRedecleared;
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean canBeRedecleared() {
        return canBeRedecleared;
    }

    @Override
    public String toString() {
        return name + " : " + type;
    }


}
