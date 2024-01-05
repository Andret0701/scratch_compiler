package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class VariableValue extends Expression {
    private String name;
    private VariableType type;
    public VariableValue(String name, VariableType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @Override
    public VariableType getType() {
        return type;
    }

    @Override
    public String toString() {
        return getName();
    }
}
