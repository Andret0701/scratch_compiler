package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.parser.VariableReference;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class VariableValue extends Expression {
    private Variable variable;
    private VariableReference reference;

    public VariableValue(Variable variable) {
        this.variable = variable;
        this.reference = null;
    }

    public VariableValue(Variable variable, VariableReference reference) {
        this.variable = variable;
        this.reference = reference;
    }

    public VariableReference getReference() {
        return reference;
    }

    @Override
    public Type getType() {
        return variable.reference(reference).getType();
    }

    @Override
    public String toString() {
        String out = variable.getName();
        if (reference != null)
            out += "." + reference;
        return out;
    }
}
