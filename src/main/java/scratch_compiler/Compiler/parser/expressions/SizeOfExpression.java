package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;

public class SizeOfExpression extends Expression {
    public SizeOfExpression(VariableValue variableValue) {
        super(1);
        setExpression(0, variableValue);
    }

    public VariableValue getVariable() {
        return (VariableValue) getExpression(0);
    }

    @Override
    public Type getType() {
        return new Type(VariableType.INT);
    }

    @Override
    public String toString() {
        return "sizeof(" + getVariable() + ")";
    }
}
