package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public class AdditionExpression extends BinaryOperationExpression {
    public AdditionExpression()
    {
        super(13);
        addDefinedFor(VariableType.INT);
        addDefinedFor(VariableType.FLOAT);
        addDefinedFor(VariableType.STRING);
    }

    @Override
    public String getOperator() {
        return "+";
    }
}
