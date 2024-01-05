package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class MultiplicationExpression extends BinaryOperationExpression {
    public MultiplicationExpression()
    {
        super(14);
        addDefinedFor(VariableType.INT);
        addDefinedFor(VariableType.FLOAT);
    }

    @Override
    public String getOperator() {
        return "*";
    }
}