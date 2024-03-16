package scratch_compiler.Compiler;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Compiler.parser.BinaryOperatorDefinition;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.types.BinaryOperatorDefinitionType;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.types.UnaryOperatorDefinitionType;

public class DeclarationTable {
    private HashMap<String, Type> declaredTypes;
    private HashMap<String, Variable> declaredVariables;
    private HashMap<String, Function> declaredFunctions;
    private HashMap<OperatorType, ArrayList<UnaryOperatorDefinition>> unaryOperators;
    private HashMap<OperatorType, ArrayList<BinaryOperatorDefinition>> binaryOperators;
    private TypeConversionTable typeConversionTable;

    public DeclarationTable() {
        declaredTypes = new HashMap<>();
        declaredVariables = new HashMap<>();
        declaredFunctions = new HashMap<>();
        unaryOperators = new HashMap<>();
        binaryOperators = new HashMap<>();
        typeConversionTable = new TypeConversionTable();
    }

    public static DeclarationTable loadDeclarationTable() {
        DeclarationTable declarationTable = new DeclarationTable();

        // Declare primitive types
        declarationTable.declareType(new Type(VariableType.BOOLEAN));
        declarationTable.declareType(new Type(VariableType.INT));
        declarationTable.declareType(new Type(VariableType.FLOAT));
        declarationTable.declareType(new Type(VariableType.STRING));
        declarationTable.declareType(new Type(VariableType.VOID));

        // Declare unary operators
        for (UnaryOperatorDefinitionType unaryOperator : UnaryOperatorDefinitionType.values()) {
            declarationTable.declareOperator(new UnaryOperatorDefinition(unaryOperator.getOperatorType(),
                    new Type(unaryOperator.getOperandType()), new Type(unaryOperator.getReturnType())));
        }

        // Declare binary operators
        for (BinaryOperatorDefinitionType binaryOperator : BinaryOperatorDefinitionType.values()) {
            declarationTable.declareOperator(new BinaryOperatorDefinition(binaryOperator.getOperatorType(),
                    new Type(binaryOperator.getLeftType()), new Type(binaryOperator.getRightType()),
                    new Type(binaryOperator.getReturnType())));
        }

        // Declare type conversions
        declarationTable.typeConversionTable.addConversion(new Type(VariableType.BOOLEAN),
                new Type(VariableType.INT));
        declarationTable.typeConversionTable.addConversion(new Type(VariableType.INT),
                new Type(VariableType.FLOAT));
        declarationTable.typeConversionTable.addConversion(new Type(VariableType.FLOAT), new Type(VariableType.STRING));

        return declarationTable;
    }

    public void declareType(Type type) {
        String name = type.getName();
        if (isTypeDeclared(name))
            throw new IllegalArgumentException("Type " + name + " is already declared");
        declaredTypes.put(name, type);
    }

    public void declareVariable(Variable variable) {
        String name = variable.getName();
        if (isVariableDeclared(name))
            throw new IllegalArgumentException("Variable " + name + " is already declared");
        declaredVariables.put(name, variable);
    }

    public void declareFunction(Function function) {
        String name = function.getName();
        if (isFunctionDeclared(name))
            throw new IllegalArgumentException("Function " + name + " is already declared");
        declaredFunctions.put(name, function);
    }

    public void declareOperator(UnaryOperatorDefinition unaryOperator) {
        OperatorType operatorType = unaryOperator.getOperatorType();
        if (!unaryOperators.containsKey(operatorType))
            unaryOperators.put(operatorType, new ArrayList<UnaryOperatorDefinition>());

        Type operandType = unaryOperator.getOperandType();
        Type returnType = unaryOperator.getReturnType();

        unaryOperators.get(operatorType).add(new UnaryOperatorDefinition(operatorType, operandType, returnType));
    }

    public void declareOperator(BinaryOperatorDefinition binaryOperator) {
        OperatorType operatorType = binaryOperator.getOperatorType();
        if (!binaryOperators.containsKey(operatorType))
            binaryOperators.put(operatorType, new ArrayList<BinaryOperatorDefinition>());

        Type leftType = binaryOperator.getLeftType();
        Type rightType = binaryOperator.getRightType();
        Type returnType = binaryOperator.getReturnType();

        binaryOperators.get(operatorType)
                .add(new BinaryOperatorDefinition(operatorType, leftType, rightType, returnType));
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

    private boolean isOperatorDeclared(OperatorType operatorType, Type operandType) {
        if (!unaryOperators.containsKey(operatorType))
            return false;

        for (UnaryOperatorDefinition unaryOperator : unaryOperators.get(operatorType)) {
            if (operandType.equals(unaryOperator.getOperandType()))
                return true;
        }

        return false;
    }

    private boolean isOperatorDeclared(OperatorType operatorType, Type leftType, Type rightType) {
        if (!binaryOperators.containsKey(operatorType))
            return false;
        for (BinaryOperatorDefinition binaryOperator : binaryOperators.get(operatorType)) {
            if (leftType.equals(binaryOperator.getLeftType()) && rightType.equals(binaryOperator.getRightType()))
                return true;
        }

        return false;
    }

    public Type getType(String typeName) {
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

    public UnaryOperatorDefinition getUnaryOperator(OperatorType operatorType, Type operandType) {
        if (!isOperatorDeclared(operatorType, operandType))
            throw new IllegalArgumentException("Operator " + operatorType + " is not declared for type " + operandType);
        for (UnaryOperatorDefinition unaryOperator : unaryOperators.get(operatorType)) {
            if (operandType.equals(unaryOperator.getOperandType()))
                return unaryOperator;
        }
        throw new IllegalArgumentException("Operator " + operatorType + " is not declared for type " + operandType);
    }

    public BinaryOperatorDefinition getBinaryOperator(OperatorType operatorType, Type leftType, Type rightType) {
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

    public void validateOperatorUsage(OperatorType operatorType, Type operandType, int line) {
        if (!isOperatorDeclared(operatorType, operandType))
            throw new IllegalArgumentException("Operator " + operatorType + " is not declared for type " + operandType
                    + " at line " + line);
    }

    public void validateOperatorUsage(OperatorType operatorType, Type leftType, Type rightType, int line) {
        if (!isOperatorDeclared(operatorType, leftType, rightType))
            throw new IllegalArgumentException(
                    "Operator " + operatorType + " is not declared for types " + leftType + " and "
                            + rightType + " at line " + line);
    }

    public void validateTypeConversion(Expression from, Type to, int line) {
        if (from == null) {
            validateTypeConversion(new Type(VariableType.VOID), to, line);
            return;
        }
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
        copy.declaredTypes = new HashMap<>(declaredTypes);
        copy.unaryOperators = new HashMap<>(unaryOperators);
        copy.binaryOperators = new HashMap<>(binaryOperators);
        copy.typeConversionTable = typeConversionTable.copy();
        return copy;
    }
}
