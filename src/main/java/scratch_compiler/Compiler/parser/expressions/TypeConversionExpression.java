package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;

public class TypeConversionExpression extends Expression {
    private Expression expression;
    private Type type;

    public TypeConversionExpression(Expression expression, Type type) {
        this.expression = expression;
        this.type = type;
    }

    public Expression getExpression() {
        return expression;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "(" + type + ") " + expression;
    }
}
