package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class LessThanExpression extends  BinaryOperationExpression {
    public LessThanExpression()
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
        return "<";
    }
}