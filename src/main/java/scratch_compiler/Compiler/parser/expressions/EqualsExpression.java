package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class EqualsExpression  extends BinaryOperationExpression {
    public EqualsExpression()
    {
        super(8);
        addDefinedFor(VariableType.BOOLEAN);
        addDefinedFor(VariableType.INT);
        addDefinedFor(VariableType.FLOAT);
        addDefinedFor(VariableType.STRING);
    }

    @Override
    public String getOperator() {
        return "==";
    }

    @Override
    public VariableType getType() {
        return VariableType.BOOLEAN;
    }
}