package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class UnaryOperator extends OperatorExpression {
    private OperatorType operatorType;

    public UnaryOperator(OperatorType operatorType, Expression operand, Type returnType) {
        super(returnType);
        addOperand(operand);

        if (operatorType.isUnary() == false)
            throw new IllegalArgumentException("OperatorType must be unary");
        this.operatorType = operatorType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public Expression getOperand() {
        return getOperand(0);
    }

    @Override
    public String getOperator() {
        return operatorType.toString();
    }
}
