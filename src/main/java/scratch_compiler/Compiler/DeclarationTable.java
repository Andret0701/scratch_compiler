package scratch_compiler.Compiler;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Compiler.parser.BinaryOperatorDefinition;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;

public class DeclarationTable {
    private HashMap<String, TypeDefinition> declaredTypes;
    private HashMap<String, Variable> declaredVariables;
    private HashMap<String, Function> declaredFunctions;
    private HashMap<String, SystemCall> declaredSystemCalls;
    private HashMap<OperatorType, ArrayList<UnaryOperatorDefinition>> unaryOperators;
    private HashMap<OperatorType, ArrayList<BinaryOperatorDefinition>> binaryOperators;
    private TypeConversionTable typeConversionTable;

    public DeclarationTable() {
        declaredTypes = new HashMap<>();
        declaredVariables = new HashMap<>();
        declaredFunctions = new HashMap<>();
        declaredSystemCalls = new HashMap<>();
        unaryOperators = new HashMap<>();
        binaryOperators = new HashMap<>();
        typeConversionTable = new TypeConversionTable();
    }

    public void declareType(TypeDefinition type) {
        String name = type.getName();
        if (isTypeDeclared(name))
            throw new IllegalArgumentException("Type " + name + " is already declared");
        declaredTypes.put(name, type);
    }

    public void declareVariable(String name, Type type) {
        if (isVariableDeclared(name))
            throw new IllegalArgumentException("Variable " + name + " is already declared");
        declaredVariables.put(name, new Variable(name, type));
    }

    public void declareFunction(Function function) {
        String name = function.getName();
        if (isFunctionDeclared(name))
            throw new IllegalArgumentException("Function " + name + " is already declared");
        declaredFunctions.put(name, function);
    }

    public void declareSystemCall(SystemCall systemCall) {
        String name = systemCall.getName();
        if (isSystemCallDeclared(name))
            throw new IllegalArgumentException("System call " + name + " is already declared");
        declaredSystemCalls.put(name, systemCall);
    }

    public void declareOperator(UnaryOperatorDefinition unaryOperator) {
        OperatorType operatorType = unaryOperator.getOperatorType();
        if (!unaryOperators.containsKey(operatorType))
            unaryOperators.put(operatorType, new ArrayList<UnaryOperatorDefinition>());

        TypeDefinition operandType = unaryOperator.getOperandType();
        TypeDefinition returnType = unaryOperator.getReturnType();

        unaryOperators.get(operatorType).add(new UnaryOperatorDefinition(operatorType, operandType, returnType));
    }

    public void declareOperator(BinaryOperatorDefinition binaryOperator) {
        OperatorType operatorType = binaryOperator.getOperatorType();
        if (!binaryOperators.containsKey(operatorType))
            binaryOperators.put(operatorType, new ArrayList<BinaryOperatorDefinition>());

        TypeDefinition leftType = binaryOperator.getLeftType();
        TypeDefinition rightType = binaryOperator.getRightType();
        TypeDefinition returnType = binaryOperator.getReturnType();

        binaryOperators.get(operatorType)
                .add(new BinaryOperatorDefinition(operatorType, leftType, rightType, returnType));
    }

    public void declareConversion(Type from, Type to) {
        typeConversionTable.addConversion(from, to);
    }

    public boolean isTypeDeclared(String typeName) {
        return declaredTypes.containsKey(typeName);
    }

    public boolean isVariableDeclared(String variableName) {
        return declaredVariables.containsKey(variableName);
    }

    public boolean isFunctionDeclared(String functionName) {
        return declaredFunctions.containsKey(functionName);
    }

    public boolean isSystemCallDeclared(String systemCallName) {
        return declaredSystemCalls.containsKey(systemCallName);
    }

    private boolean isOperatorDeclared(OperatorType operatorType, TypeDefinition operandType) {
        if (!unaryOperators.containsKey(operatorType))
            return false;

        for (UnaryOperatorDefinition unaryOperator : unaryOperators.get(operatorType)) {
            if (operandType.equals(unaryOperator.getOperandType()))
                return true;
        }

        return false;
    }

    private boolean isOperatorDeclared(OperatorType operatorType, TypeDefinition leftType, TypeDefinition rightType) {
        if (!binaryOperators.containsKey(operatorType))
            return false;
        for (BinaryOperatorDefinition binaryOperator : binaryOperators.get(operatorType)) {
            if (leftType.equals(binaryOperator.getLeftType()) && rightType.equals(binaryOperator.getRightType()))
                return true;
        }

        return false;
    }

    public TypeDefinition getType(String typeName) {
        if (!isTypeDeclared(typeName))
            throw new IllegalArgumentException("Type " + typeName + " is not declared");
        return declaredTypes.get(typeName);
    }

    public Variable getVariable(String variableName) {
        if (!isVariableDeclared(variableName))
            throw new IllegalArgumentException("Variable " + variableName + " is not declared");
        return declaredVariables.get(variableName);
    }

    public Function getFunction(String functionName) {
        if (!isFunctionDeclared(functionName))
            throw new IllegalArgumentException("Function " + functionName + " is not declared");
        return declaredFunctions.get(functionName);
    }

    public SystemCall getSystemCall(String systemCallName) {
        if (!isSystemCallDeclared(systemCallName))
            throw new IllegalArgumentException("System call " + systemCallName + " is not declared");
        return declaredSystemCalls.get(systemCallName);
    }

    public UnaryOperatorDefinition getUnaryOperator(OperatorType operatorType, TypeDefinition operandType) {
        if (!isOperatorDeclared(operatorType, operandType))
            throw new IllegalArgumentException("Operator " + operatorType + " is not declared for type " + operandType);
        for (UnaryOperatorDefinition unaryOperator : unaryOperators.get(operatorType)) {
            if (operandType.equals(unaryOperator.getOperandType()))
                return unaryOperator;
        }
        throw new IllegalArgumentException("Operator " + operatorType + " is not declared for type " + operandType);
    }

    public BinaryOperatorDefinition getBinaryOperator(OperatorType operatorType, TypeDefinition leftType,
            TypeDefinition rightType) {
        if (!isOperatorDeclared(operatorType, leftType, rightType))
            throw new IllegalArgumentException(
                    "Operator " + operatorType + " is not declared for types " + leftType + " and "
                            + rightType);
        for (BinaryOperatorDefinition binaryOperator : binaryOperators.get(operatorType)) {
            if (leftType.equals(binaryOperator.getLeftType()) && rightType.equals(binaryOperator.getRightType()))
                return binaryOperator;
        }
        throw new IllegalArgumentException(
                "Operator " + operatorType + " is not declared for types " + leftType + " and "
                        + rightType);
    }

    public void validateVariableDeclaration(String variableName, int line) {
        if (isVariableDeclared(variableName))
            throw new IllegalArgumentException("Variable " + variableName + " is already declared at line " + line);
    }

    public void validateFunctionDeclaration(String functionName, int line) {
        if (isFunctionDeclared(functionName))
            throw new IllegalArgumentException("Function " + functionName + " is already declared at line " + line);
    }

    public void validateVariableUsage(String variableName, int line) {
        if (!isVariableDeclared(variableName))
            throw new IllegalArgumentException("Variable " + variableName + " is not declared at line " + line);
    }

    public void validateFunctionUsage(String functionName, int line) {
        if (!isFunctionDeclared(functionName))
            throw new IllegalArgumentException("Function " + functionName + " is not declared at line " + line);
    }

    public void validateSystemCallUsage(String systemCallName, int line) {
        if (!isSystemCallDeclared(systemCallName))
            throw new IllegalArgumentException("System call " + systemCallName + " is not declared at line " + line);
    }

    public void validateOperatorUsage(OperatorType operatorType, Type operandType, int line) {
        if (operandType.isArray())
            throw new IllegalArgumentException(
                    "Cannot use operator " + operatorType + " on array type at line " + line);
        validateOperatorUsage(operatorType, operandType.getType(), line);
    }

    public void validateOperatorUsage(OperatorType operatorType, TypeDefinition operandType, int line) {
        if (!isOperatorDeclared(operatorType, operandType))
            throw new IllegalArgumentException("Operator " + operatorType + " is not declared for type " + operandType
                    + " at line " + line);
    }

    public void validateOperatorUsage(OperatorType operatorType, Type leftType, Type rightType, int line) {
        if (leftType.isArray() || rightType.isArray())
            throw new IllegalArgumentException(
                    "Cannot use operator " + operatorType + " on array type at line " + line);

        validateOperatorUsage(operatorType, leftType.getType(), rightType.getType(), line);
    }

    public void validateOperatorUsage(OperatorType operatorType, TypeDefinition leftType, TypeDefinition rightType,
            int line) {
        if (!isOperatorDeclared(operatorType, leftType, rightType))
            throw new IllegalArgumentException(
                    "Operator " + operatorType + " is not declared for types " + leftType + " and "
                            + rightType + " at line " + line);
    }

    public void validateTypeConversion(Expression from, Type to, int line) {
        if (from == null)
            throw new IllegalArgumentException("Cannot convert null to " + to + " at line " + line);
        validateTypeConversion(from.getType(), to, line);
    }

    public void validateTypeConversion(Type from, Type to, int line) {
        if (!typeConversionTable.canConvert(from, to))
            throw new IllegalArgumentException("Cannot convert " + from + " to " + to + " at line " + line);
    }

    public Expression convertExpression(Expression expression, Type to) {
        if (expression == null)
            return null;
        return typeConversionTable.convert(expression, to);
    }

    public DeclarationTable copy() {
        DeclarationTable copy = new DeclarationTable();
        copy.declaredVariables = new HashMap<>(declaredVariables);
        copy.declaredFunctions = new HashMap<>(declaredFunctions);
        copy.declaredSystemCalls = new HashMap<>(declaredSystemCalls);
        copy.declaredTypes = new HashMap<>(declaredTypes);
        copy.unaryOperators = new HashMap<>(unaryOperators);
        copy.binaryOperators = new HashMap<>(binaryOperators);
        copy.typeConversionTable = typeConversionTable.copy();
        return copy;
    }
}
