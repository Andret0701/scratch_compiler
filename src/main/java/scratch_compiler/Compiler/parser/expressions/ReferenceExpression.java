package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;

public class ReferenceExpression extends Expression {
    private String reference;

    public ReferenceExpression(String reference, Expression expression) {
        super(1);

        if (expression.getType().isArray())
            throw new IllegalArgumentException("Cannot reference an array");

        if (!expression.getType().getType().containsReference(reference))
            throw new IllegalArgumentException("The reference does not match the type of the expression");

        this.reference = reference;
        setExpression(0, expression);
    }

    public String getReference() {
        return reference;
    }

    public Expression getExpression() {
        return getExpression(0);
    }

    @Override
    public Type getType() {
        return new Type(getExpression().getType().getType().reference(reference));
    }

    @Override
    public String toString() {
        return getExpression().toString() + ". " + reference;
    }
}