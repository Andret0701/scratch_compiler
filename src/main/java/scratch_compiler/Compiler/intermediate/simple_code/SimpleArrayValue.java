package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class SimpleArrayValue extends Expression {
    private String name;
    private VariableType type;
    private Expression index;

    public SimpleArrayValue(String name, VariableType type, Expression index) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @Override
    public TypeDefinition getType() {
        return new TypeDefinition(type);
    }

    public Expression getIndex() {
        return index;
    }

}
