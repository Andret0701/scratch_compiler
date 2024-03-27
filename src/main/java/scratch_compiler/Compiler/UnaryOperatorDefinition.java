package scratch_compiler.Compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class UnaryOperatorDefinition {
    private OperatorType operatorType;
    private TypeDefinition operandType;
    private TypeDefinition returnType;

    public UnaryOperatorDefinition(OperatorType operatorType, TypeDefinition operandType, TypeDefinition returnType) {
        this.operatorType = operatorType;
        this.operandType = operandType;
        this.returnType = returnType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public TypeDefinition getOperandType() {
        return operandType;
    }

    public TypeDefinition getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return "UnaryOperatorDefinition{" +
                "operatorType=" + operatorType +
                ", operandType=" + operandType +
                ", returnType=" + returnType +
                '}';
    }

}
