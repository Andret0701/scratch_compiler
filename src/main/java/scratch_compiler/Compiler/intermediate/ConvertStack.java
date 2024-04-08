package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.TypeField;
import scratch_compiler.Compiler.intermediate.simple_code.ArrayPop;
import scratch_compiler.Compiler.intermediate.simple_code.Pop;
import scratch_compiler.Compiler.intermediate.simple_code.Push;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.ArrayValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StructValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.WhileStatement;

public class ConvertStack {

    public static ArrayList<Statement> push(Expression expression, IntermediateTable table) {
        // if array then its either a arrayValue or a variableValue
        ArrayList<Statement> statements = new ArrayList<>();
        if (expression == null) {
            return statements;
        }

        boolean isArray = expression.getType().isArray();
        if (isArray) {
            if (expression instanceof ArrayValue) {
                statements.addAll(pushArray((ArrayValue) expression, table));
                return statements;
            }

            if (expression instanceof VariableValue) {
                VariableValue variableValue = (VariableValue) expression;
                statements.addAll(pushArray(variableValue.getName(), variableValue.getType().getType(),
                        table));
                return statements;
            }

            throw new RuntimeException("Unsupported expression type: " + expression.getClass().getName());
        }

        if (expression instanceof StructValue) {
            statements.addAll(pushStructValue((StructValue) expression, table));
            return statements;
        }

        statements.add(new Push(expression));
        return statements;
    }

    public static ArrayList<Statement> pushArray(ArrayValue arrayValue, IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (int i = arrayValue.getValues().size() - 1; i >= 0; i--) {
            Expression element = arrayValue.getValues().get(i);
            statements.addAll(push(element, table));
        }

        statements.addAll(push(new IntValue(arrayValue.getValues().size()), table));
        return statements;
    }

    public static ArrayList<Statement> pushArray(String name, TypeDefinition type, IntermediateTable table) {
        String size = "size:" + name;
        ArrayList<Statement> statements = pushArray(name, size, type, table);
        statements.add(new Push(new SimpleVariableValue(size, VariableType.INT)));
        return statements;
    }

    private static ArrayList<Statement> pushArray(String name, String size, TypeDefinition type,
            IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();

        String iterator = table.getUniqueTemp("iterator");
        statements.add(new SimpleVariableDeclaration(iterator, VariableType.INT));
        statements.add(new SimpleVariableAssignment(iterator, new BinaryOperator(OperatorType.SUBTRACTION,
                new SimpleVariableValue(size, VariableType.INT), new IntValue(1),
                new Type(VariableType.INT))));

        Scope whileBody = new Scope();
        whileBody.addAllStatements(pushArray(name, type, new SimpleVariableValue(iterator, VariableType.INT)));
        whileBody.addStatement(new SimpleVariableAssignment(iterator, new BinaryOperator(OperatorType.SUBTRACTION,
                new SimpleVariableValue(iterator, VariableType.INT), new IntValue(1),
                new Type(VariableType.INT))));

        WhileStatement whileStatement = new WhileStatement(new BinaryOperator(OperatorType.GREATER_THAN,
                new SimpleVariableValue(iterator, VariableType.INT),
                new IntValue(-1), new Type(VariableType.BOOLEAN)),
                whileBody);

        statements.add(whileStatement);

        return statements;
    }

    private static ArrayList<Statement> pushArray(String name, TypeDefinition type, Expression index) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (type.getType() != VariableType.STRUCT) {
            statements.add(new Push(new SimpleArrayValue(name, type.getType(), index)));
            return statements;
        }

        for (TypeField field : type.getFields().reversed()) {
            String fieldName = name + "." + field.getName();
            statements.addAll(pushArray(fieldName, field.getType(), index));
        }

        return statements;
    }

    public static ArrayList<Statement> pushStructValue(StructValue structValue, IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (int i = structValue.getFields().size() - 1; i >= 0; i--) {
            Expression value = structValue.getFields().get(i);
            statements.addAll(push(value, table));
        }

        return statements;
    }

    public static ArrayList<Statement> popVariable(String name, TypeDefinition type) {
        ArrayList<Statement> statements = new ArrayList<>();

        if (type.getType() != VariableType.STRUCT) {
            statements.add(new Pop(name));
            return statements;
        }

        for (TypeField field : type.getFields()) {
            String fieldName = name + "." + field.getName();
            statements.addAll(popVariable(fieldName, field.getType()));
        }

        return statements;
    }

    public static ArrayList<Statement> popArray(String name, String size, TypeDefinition type,
            IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();

        String iterator = table.getUniqueTemp("iterator");

        Scope scope = new Scope();

        scope.addStatement(new SimpleVariableDeclaration(iterator, VariableType.INT));
        scope.addStatement(new SimpleVariableAssignment(iterator, new IntValue(0)));

        Scope whileBody = new Scope();
        whileBody.addAllStatements(popArray(name, type, new SimpleVariableValue(iterator, VariableType.INT)));
        whileBody.addStatement(new SimpleVariableAssignment(iterator, new BinaryOperator(OperatorType.ADDITION,
                new SimpleVariableValue(iterator, VariableType.INT), new IntValue(1),
                new Type(VariableType.INT))));

        WhileStatement whileStatement = new WhileStatement(new BinaryOperator(OperatorType.LESS_THAN,
                new SimpleVariableValue(iterator, VariableType.INT),
                new SimpleVariableValue(size, VariableType.INT), new Type(VariableType.BOOLEAN)),
                whileBody);

        scope.addStatement(whileStatement);
        statements.add(scope);

        return statements;
    }

    private static ArrayList<Statement> popArray(String name, TypeDefinition type, Expression index) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (type.getType() != VariableType.STRUCT) {
            statements.add(new ArrayPop(name, index));
            return statements;
        }

        for (TypeField field : type.getFields()) {
            String fieldName = name + "." + field.getName();
            statements.addAll(popArray(fieldName, field.getType(), index));
        }

        return statements;
    }

}
