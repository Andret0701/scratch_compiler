package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class SubtractionExpression  extends BinaryOperationExpression {
    public SubtractionExpression()
    {
        super(13);
        addDefinedFor(VariableType.INT);
        addDefinedFor(VariableType.FLOAT);
    }

    @Override
    public String getOperator() {
        return "-";
    }
}
