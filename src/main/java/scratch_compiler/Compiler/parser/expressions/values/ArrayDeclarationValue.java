package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class ArrayDeclarationValue extends Expression {
    private Type type;
    private Expression size;

    public ArrayDeclarationValue(TypeDefinition type, Expression size) {
        this.type = new Type(type, true);
        this.size = size;
    }

    @Override
    public Type getType() {
        return type;
    }

    public Expression getSize() {
        return size;
    }

    @Override
    public String toString() {
        return type.getType() + "[" + size + "]";
    }
}
