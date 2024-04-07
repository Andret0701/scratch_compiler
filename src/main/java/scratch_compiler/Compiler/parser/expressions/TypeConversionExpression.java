package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;

public class TypeConversionExpression extends Expression {
    private Type type;

    public TypeConversionExpression(Expression expression, Type type) {
        super(1);
        setExpression(0, expression);
        this.type = type;
    }

    public Expression getExpression() {
        return getExpression(0);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "(" + type + ") " + getExpression();
    }
}
