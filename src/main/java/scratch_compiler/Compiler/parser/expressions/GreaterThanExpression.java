package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class GreaterThanExpression extends  BinaryOperationExpression {
    public GreaterThanExpression()
    {
        super(10);
        addDefinedFor(VariableType.INT);
        addDefinedFor(VariableType.FLOAT);
    }

    @Override
    public VariableType getType() {
        return VariableType.BOOLEAN;
    }

    @Override
    public String getOperator() {
        return ">";
    }
}
