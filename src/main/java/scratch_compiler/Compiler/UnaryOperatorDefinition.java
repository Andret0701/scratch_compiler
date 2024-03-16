package scratch_compiler.Compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class UnaryOperatorDefinition {
    private OperatorType operatorType;
    private Type operandType;
    private Type returnType;

    public UnaryOperatorDefinition(OperatorType operatorType, Type operandType, Type returnType) {
        this.operatorType = operatorType;
        this.operandType = operandType;
        this.returnType = returnType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public Type getOperandType() {
        return operandType;
    }

    public Type getReturnType() {
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
