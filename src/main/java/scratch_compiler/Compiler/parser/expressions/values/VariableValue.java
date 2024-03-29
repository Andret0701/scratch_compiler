package scratch_compiler.Compiler.parser.expressions.values;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class VariableValue extends Expression {
    private Variable variable;

    public VariableValue(String name, Type type) {
        this.variable = new Variable(name, type);
    }

    public String getName() {
        return variable.getName();
    }

    @Override
    public Type getType() {
        return variable.getType();
    }

    @Override
    public String toString() {
        return variable.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VariableValue) {
            VariableValue other = (VariableValue) obj;
            return variable.equals(other.variable);
        }
        return false;
    }
}
