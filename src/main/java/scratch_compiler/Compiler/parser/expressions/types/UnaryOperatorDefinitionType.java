package scratch_compiler.Compiler.parser.expressions.types;

import scratch_compiler.Compiler.parser.VariableType;

public enum UnaryOperatorDefinitionType {
    INT_UNARY_NEGATION(OperatorType.UNARY_NEGATION, VariableType.INT, VariableType.INT),
    FLOAT_UNARY_NEGATION(OperatorType.UNARY_NEGATION, VariableType.FLOAT, VariableType.FLOAT),
    NOT(OperatorType.NOT, VariableType.BOOLEAN, VariableType.BOOLEAN);

    private OperatorType operatorType;
    private VariableType returnType;
    private VariableType operandType;

    UnaryOperatorDefinitionType(OperatorType operatorType, VariableType operandType, VariableType returnType) {
        this.operatorType = operatorType;
        this.returnType = returnType;
        this.operandType = operandType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public VariableType getReturnType() {
        return returnType;
    }

    public VariableType getOperandType() {
        return operandType;
    }
}
