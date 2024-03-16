package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class BinaryOperator extends OperatorExpression {
    private OperatorType operatorType;

    public BinaryOperator(OperatorType operatorType, Expression left, Expression right, Type returnType) {
        super(returnType);
        addOperand(left);
        addOperand(right);

        if (operatorType.isUnary())
            throw new IllegalArgumentException("OperatorType must be unary");
        this.operatorType = operatorType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public Expression getLeft() {
        return getOperand(0);
    }

    public Expression getRight() {
        return getOperand(1);
    }

    @Override
    public String getOperator() {
        return operatorType.toString();
    }
}
