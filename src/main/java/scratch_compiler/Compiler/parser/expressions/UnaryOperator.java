package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class UnaryOperator extends Expression {
    private OperatorType operatorType;
    private Type returnType;

    public UnaryOperator(OperatorType operatorType, Expression operand, Type returnType) {
        super(1);
        setExpression(0, operand);

        if (operatorType.isUnary() == false)
            throw new IllegalArgumentException("OperatorType must be unary");
        this.operatorType = operatorType;
        this.returnType = returnType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public Expression getOperand() {
        return getExpression(0);
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
        return getOperator() + "" + getOperand();
    }
}
