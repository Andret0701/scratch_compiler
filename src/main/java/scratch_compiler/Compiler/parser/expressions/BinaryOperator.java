package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class BinaryOperator extends Expression {
    private OperatorType operatorType;
    private Type returnType;

    public BinaryOperator(OperatorType operatorType, Expression left, Expression right, Type returnType) {
        super(2);
        setExpression(0, left);
        setExpression(1, right);

        if (operatorType.isUnary())
            throw new IllegalArgumentException("OperatorType must be unary");
        this.operatorType = operatorType;
        this.returnType = returnType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public Expression getLeft() {
        return getExpression(0);
    }

    public Expression getRight() {
        return getExpression(1);
    }

    @Override
    public Type getType() {
        return returnType;
    }

    public String getOperator() {
        return operatorType.getName();
    }

    @Override
    public String toString() {
        return getLeft() + " " + getOperator() + " " + getRight();
    }
}
