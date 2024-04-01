package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class SimpleVariableValue extends Expression {
    private String name;
    private VariableType type;

    public SimpleVariableValue(String name, VariableType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return new Type(type);
    }

    @Override
    public String toString() {
        return name;
    }

}
