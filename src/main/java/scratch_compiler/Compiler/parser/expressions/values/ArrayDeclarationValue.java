package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class ArrayDeclarationValue extends Expression {
    private Type type;

    public ArrayDeclarationValue(TypeDefinition type, Expression size) {
        super(1);
        this.type = new Type(type, true);
        setExpression(0, size);
    }

    @Override
    public Type getType() {
        return type;
    }

    public Expression getSize() {
        return getExpression(0);
    }

    @Override
    public String toString() {
        return type.getType() + "[" + getSize() + "]";
    }
}
