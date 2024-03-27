package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class BinaryOperatorDefinition {
    private OperatorType operatorType;
    private TypeDefinition leftType;
    private TypeDefinition rightType;
    private TypeDefinition returnType;

    public BinaryOperatorDefinition(OperatorType operatorType, TypeDefinition leftType, TypeDefinition rightType,
            TypeDefinition returnType) {
        this.operatorType = operatorType;
        this.leftType = leftType;
        this.rightType = rightType;
        this.returnType = returnType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public TypeDefinition getLeftType() {
        return leftType;
    }

    public TypeDefinition getRightType() {
        return rightType;
    }

    public TypeDefinition getReturnType() {
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
