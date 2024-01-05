package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class DivisionExpression extends BinaryOperationExpression {
    public DivisionExpression()
    {
        super(14);
        addDefinedFor(VariableType.FLOAT);
    }

    @Override
    public String getOperator() {
        return "/";
    }
}