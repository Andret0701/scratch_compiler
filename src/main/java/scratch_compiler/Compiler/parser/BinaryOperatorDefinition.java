package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class BinaryOperatorDefinition {
    private OperatorType operatorType;
    private Type leftType;
    private Type rightType;
    private Type returnType;

    public BinaryOperatorDefinition(OperatorType operatorType, Type leftType, Type rightType, Type returnType) {
        this.operatorType = operatorType;
        this.leftType = leftType;
        this.rightType = rightType;
        this.returnType = returnType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public Type getLeftType() {
        return leftType;
    }

    public Type getRightType() {
        return rightType;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return "BinaryOperatorDefinition{" +
                "operatorType=" + operatorType +
                ", leftType=" + leftType +
                ", rightType=" + rightType +
                ", returnType=" + returnType +
                '}';
    }
}
