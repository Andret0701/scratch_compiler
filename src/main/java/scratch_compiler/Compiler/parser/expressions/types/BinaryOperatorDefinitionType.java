package scratch_compiler.Compiler.parser.expressions.types;

import scratch_compiler.Compiler.parser.VariableType;

public enum BinaryOperatorDefinitionType {
    INT_MULTIPLICATION(OperatorType.MULTIPLICATION, VariableType.INT, VariableType.INT, VariableType.INT),
    FLOAT_MULTIPLICATION(OperatorType.MULTIPLICATION, VariableType.FLOAT, VariableType.FLOAT, VariableType.FLOAT),
    INT_FLOAT_MULTIPLICATION(OperatorType.MULTIPLICATION, VariableType.INT, VariableType.FLOAT, VariableType.FLOAT),
    FLOAT_INT_MULTIPLICATION(OperatorType.MULTIPLICATION, VariableType.FLOAT, VariableType.INT, VariableType.FLOAT),

    INT_DIVISION(OperatorType.DIVISION, VariableType.INT, VariableType.INT, VariableType.INT),
    FLOAT_DIVISION(OperatorType.DIVISION, VariableType.FLOAT, VariableType.FLOAT, VariableType.FLOAT),
    INT_FLOAT_DIVISION(OperatorType.DIVISION, VariableType.INT, VariableType.FLOAT, VariableType.FLOAT),
    FLOAT_INT_DIVISION(OperatorType.DIVISION, VariableType.FLOAT, VariableType.INT, VariableType.FLOAT),

    INT_MODULUS(OperatorType.MODULUS, VariableType.INT, VariableType.INT, VariableType.INT),
    FLOAT_MODULUS(OperatorType.MODULUS, VariableType.FLOAT, VariableType.FLOAT, VariableType.FLOAT),
    INT_FLOAT_MODULUS(OperatorType.MODULUS, VariableType.INT, VariableType.FLOAT, VariableType.FLOAT),
    FLOAT_INT_MODULUS(OperatorType.MODULUS, VariableType.FLOAT, VariableType.INT, VariableType.FLOAT),

    INT_ADDITION(OperatorType.ADDITION, VariableType.INT, VariableType.INT, VariableType.INT),
    FLOAT_ADDITION(OperatorType.ADDITION, VariableType.FLOAT, VariableType.FLOAT, VariableType.FLOAT),
    INT_FLOAT_ADDITION(OperatorType.ADDITION, VariableType.INT, VariableType.FLOAT, VariableType.FLOAT),
    FLOAT_INT_ADDITION(OperatorType.ADDITION, VariableType.FLOAT, VariableType.INT, VariableType.FLOAT),
    STRING_ADDITION(OperatorType.ADDITION, VariableType.STRING, VariableType.STRING, VariableType.STRING),

    INT_SUBTRACTION(OperatorType.SUBTRACTION, VariableType.INT, VariableType.INT, VariableType.INT),
    FLOAT_SUBTRACTION(OperatorType.SUBTRACTION, VariableType.FLOAT, VariableType.FLOAT, VariableType.FLOAT),
    INT_FLOAT_SUBTRACTION(OperatorType.SUBTRACTION, VariableType.INT, VariableType.FLOAT, VariableType.FLOAT),
    FLOAT_INT_SUBTRACTION(OperatorType.SUBTRACTION, VariableType.FLOAT, VariableType.INT, VariableType.FLOAT),

    INT_LESS_THAN(OperatorType.LESS_THAN, VariableType.INT, VariableType.INT, VariableType.BOOLEAN),
    FLOAT_LESS_THAN(OperatorType.LESS_THAN, VariableType.FLOAT, VariableType.FLOAT, VariableType.BOOLEAN),
    INT_FLOAT_LESS_THAN(OperatorType.LESS_THAN, VariableType.INT, VariableType.FLOAT, VariableType.BOOLEAN),
    FLOAT_INT_LESS_THAN(OperatorType.LESS_THAN, VariableType.FLOAT, VariableType.INT, VariableType.BOOLEAN),

    STRING_LESS_THAN(OperatorType.LESS_THAN, VariableType.STRING, VariableType.STRING, VariableType.BOOLEAN),
    INT_GREATER_THAN(OperatorType.GREATER_THAN, VariableType.INT, VariableType.INT, VariableType.BOOLEAN),
    FLOAT_GREATER_THAN(OperatorType.GREATER_THAN, VariableType.FLOAT, VariableType.FLOAT, VariableType.BOOLEAN),
    INT_FLOAT_GREATER_THAN(OperatorType.GREATER_THAN, VariableType.INT, VariableType.FLOAT, VariableType.BOOLEAN),
    FLOAT_INT_GREATER_THAN(OperatorType.GREATER_THAN, VariableType.FLOAT, VariableType.INT, VariableType.BOOLEAN),
    STRING_GREATER_THAN(OperatorType.GREATER_THAN, VariableType.STRING, VariableType.STRING, VariableType.BOOLEAN),

    INT_LESS_EQUALS(OperatorType.LESS_EQUALS, VariableType.INT, VariableType.INT, VariableType.BOOLEAN),
    FLOAT_LESS_EQUALS(OperatorType.LESS_EQUALS, VariableType.FLOAT, VariableType.FLOAT, VariableType.BOOLEAN),
    INT_FLOAT_LESS_EQUALS(OperatorType.LESS_EQUALS, VariableType.INT, VariableType.FLOAT, VariableType.BOOLEAN),
    FLOAT_INT_LESS_EQUALS(OperatorType.LESS_EQUALS, VariableType.FLOAT, VariableType.INT, VariableType.BOOLEAN),
    STRING_LESS_EQUALS(OperatorType.LESS_EQUALS, VariableType.STRING, VariableType.STRING, VariableType.BOOLEAN),

    INT_GREATER_EQUALS(OperatorType.GREATER_EQUALS, VariableType.INT, VariableType.INT, VariableType.BOOLEAN),
    FLOAT_GREATER_EQUALS(OperatorType.GREATER_EQUALS, VariableType.FLOAT, VariableType.FLOAT, VariableType.BOOLEAN),
    INT_FLOAT_GREATER_EQUALS(OperatorType.GREATER_EQUALS, VariableType.INT, VariableType.FLOAT, VariableType.BOOLEAN),
    FLOAT_INT_GREATER_EQUALS(OperatorType.GREATER_EQUALS, VariableType.FLOAT, VariableType.INT, VariableType.BOOLEAN),
    STRING_GREATER_EQUALS(OperatorType.GREATER_EQUALS, VariableType.STRING, VariableType.STRING, VariableType.BOOLEAN),

    BOOLEAN_EQUALS(OperatorType.EQUALS, VariableType.BOOLEAN, VariableType.BOOLEAN, VariableType.BOOLEAN),
    INT_EQUALS(OperatorType.EQUALS, VariableType.INT, VariableType.INT, VariableType.BOOLEAN),
    FLOAT_EQUALS(OperatorType.EQUALS, VariableType.FLOAT, VariableType.FLOAT, VariableType.BOOLEAN),
    INT_FLOAT_EQUALS(OperatorType.EQUALS, VariableType.INT, VariableType.FLOAT, VariableType.BOOLEAN),
    FLOAT_INT_EQUALS(OperatorType.EQUALS, VariableType.FLOAT, VariableType.INT, VariableType.BOOLEAN),
    STRING_EQUALS(OperatorType.EQUALS, VariableType.STRING, VariableType.STRING, VariableType.BOOLEAN),

    BOOLEAN_NOT_EQUALS(OperatorType.NOT_EQUALS, VariableType.BOOLEAN, VariableType.BOOLEAN, VariableType.BOOLEAN),
    INT_NOT_EQUALS(OperatorType.NOT_EQUALS, VariableType.INT, VariableType.INT, VariableType.BOOLEAN),
    FLOAT_NOT_EQUALS(OperatorType.NOT_EQUALS, VariableType.FLOAT, VariableType.FLOAT, VariableType.BOOLEAN),
    INT_FLOAT_NOT_EQUALS(OperatorType.NOT_EQUALS, VariableType.INT, VariableType.FLOAT, VariableType.BOOLEAN),
    FLOAT_INT_NOT_EQUALS(OperatorType.NOT_EQUALS, VariableType.FLOAT, VariableType.INT, VariableType.BOOLEAN),
    STRING_NOT_EQUALS(OperatorType.NOT_EQUALS, VariableType.STRING, VariableType.STRING, VariableType.BOOLEAN),

    BOOLEAN_AND(OperatorType.AND, VariableType.BOOLEAN, VariableType.BOOLEAN, VariableType.BOOLEAN),
    BOOLEAN_OR(OperatorType.OR, VariableType.BOOLEAN, VariableType.BOOLEAN, VariableType.BOOLEAN);

    private OperatorType operatorType;
    private VariableType returnType;
    private VariableType leftType;
    private VariableType rightType;

    BinaryOperatorDefinitionType(OperatorType operatorType, VariableType leftType, VariableType rightType,
            VariableType returnType) {
        this.operatorType = operatorType;
        this.returnType = returnType;
        this.leftType = leftType;
        this.rightType = rightType;
    }

    public OperatorType getOperatorType() {
        return operatorType;
    }

    public VariableType getReturnType() {
        return returnType;
    }

    public VariableType getLeftType() {
        return leftType;
    }

    public VariableType getRightType() {
        return rightType;
    }
}
