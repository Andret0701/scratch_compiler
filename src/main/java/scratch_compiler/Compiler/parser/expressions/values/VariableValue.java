package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.parser.VariableReference;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class VariableValue extends Expression {
    private Variable variable;
    private VariableReference reference;

    public VariableValue(String name, VariableReference reference, Type type) {
        this(name, reference, type, null);
    }

    public VariableValue(String name, VariableReference reference, Type type, Expression index) {
        super(index == null ? 0 : 1);

        if (type.isArray() && index == null && reference != null)
            throw new RuntimeException("Can not reference an array without an index");

        this.reference = reference;
        this.variable = new Variable(name, type);

        if (index != null)
            setExpression(0, index);
    }

    public Expression getIndex() {
        if (getExpressionCount() == 0)
            return null;
        return getExpression(0);
    }

    public boolean isArray() {
        return variable.getType().isArray();
    }

    public VariableReference getReference() {
        return reference;
    }

    @Override
    public Type getType() {
        if (isArray() && getIndex() == null)
            return variable.getType();

        if (reference == null)
            return new Type(variable.getType(), false);

        return new Type(variable.getType().getType().reference(reference));
    }

    @Override
    public String toString() {
        String out = variable.getName();
        if (getIndex() != null)
            out += "[" + getIndex() + "]";

        if (reference != null)
            out += "." + reference;

        return out;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VariableValue) {
            VariableValue other = (VariableValue) obj;
            return reference.equals(other.reference) && getType().equals(other.getType());
        }
        return false;
    }

    public String getName() {
        return variable.getName();
    }
}
