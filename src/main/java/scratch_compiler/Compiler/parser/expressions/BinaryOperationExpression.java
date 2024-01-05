package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.parser.VariableType;

public abstract class BinaryOperationExpression extends OperationExpression {
    private Expression left;
    private Expression right;

    public BinaryOperationExpression(int precedence) {
        super(precedence);

    }

    public void setLeft(Expression left) {
        if (!isDefinedFor(left.getType()))
            throw new RuntimeException("Operation not supported for " + left.getType());
        this.left = left;
    }

    public void setRight(Expression right) {
        if (!isDefinedFor(right.getType()))
            throw new RuntimeException("Operation not supported for " + right.getType());
        this.right = right;
    }

    @Override
    public VariableType getType() {
        if (left==null && right==null)
            return null;
        if (left==null)
            return getOutputType(right.getType());
        if (right==null)
            return getOutputType(left.getType());
        
        VariableType leftType = left.getType();
        VariableType rightType = right.getType();
        if (leftType.canBeConvertedTo(rightType))
            return getOutputType(rightType);
        return getOutputType(leftType);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "( " + left + " " + getOperator() + " " + right + " )";
    }
}