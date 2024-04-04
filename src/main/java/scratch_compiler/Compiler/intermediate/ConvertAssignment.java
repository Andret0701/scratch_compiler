package scratch_compiler.Compiler.intermediate;

import java.sql.Struct;
import java.util.ArrayList;

import scratch_compiler.Compiler.SystemCall;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.TypeField;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.intermediate.simple_code.VariableReference;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.IndexExpression;
import scratch_compiler.Compiler.parser.expressions.ReferenceExpression;
import scratch_compiler.Compiler.parser.expressions.values.ArrayDeclarationValue;
import scratch_compiler.Compiler.parser.expressions.values.ArrayValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StructValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ConvertAssignment {
    public static ArrayList<Statement> declareVariable(String name, TypeDefinition type) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (type.getType() != VariableType.STRUCT) {
            statements.add(new SimpleVariableDeclaration(name, type.getType()));
            return statements;
        }

        for (TypeField field : type.getFields()) {
            String fieldName = name + "." + field.getName();
            TypeDefinition fieldType = field.getType();
            statements.addAll(declareVariable(fieldName, fieldType));
        }

        return statements;
    }

    public static ArrayList<Statement> declareArray(String name, String size, TypeDefinition type) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (type.getType() != VariableType.STRUCT) {
            statements.add(
                    new SimpleArrayDeclaration(name, type.getType(), new SimpleVariableValue(size, VariableType.INT)));
            return statements;
        }

        for (TypeField field : type.getFields()) {
            String fieldName = name + "." + field.getName();
            statements.addAll(declareArray(fieldName, size, field.getType()));
        }

        return statements;
    }

    public static ArrayList<Statement> convert(Assignment assignment) {
        return convert((VariableReference) assignment.getVariable(), assignment.getExpression());
    }

    public static ArrayList<Statement> convert(VariableReference variable, Expression expression) {
        if (!variable.getType().equals(expression.getType()))
            throw new RuntimeException("Invalid type for assignment: " + variable.getType() + " "
                    + expression);

        ArrayList<Statement> statements = new ArrayList<>();

        if (expression instanceof StructValue) {
            StructValue structValue = (StructValue) expression;
            System.out.println(variable.getName() + " " + structValue);
            statements.addAll(convert(variable.getName(), structValue));
        } else if (expression instanceof VariableReference) {
            VariableReference value = (VariableReference) expression;
            statements.addAll(convert(variable.getName(), value));
        } else {
            statements.add(new SimpleVariableAssignment(variable.getName(), expression));
        }

        return statements;
    }

    private static ArrayList<Statement> convert(String name, VariableReference value) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (value.getType().getType().getType() != VariableType.STRUCT) {
            statements.add(new SimpleVariableAssignment(name,
                    ConvertExpression.convertVariableReference(value)));
            return statements;
        }

        for (TypeField field : value.getType().getType().getFields()) {
            String fieldName = name + "." + field.getName();
            TypeDefinition fieldType = field.getType();
            statements.addAll(convert(name + "." + field.getName(),
                    new VariableReference(value.getName() + "." + field.getName(), new Type(fieldType),
                            value.getIndex())));
        }

        return statements;
    }

    private static ArrayList<Statement> convert(String name, StructValue value) {
        ArrayList<Statement> statements = new ArrayList<>();

        for (TypeField field : value.getType().getType().getFields()) {
            TypeDefinition fieldType = field.getType();
            Expression fieldValue = value.getField(field.getName());
            if (fieldType.getType() != VariableType.STRUCT) {
                statements.add(new SimpleVariableAssignment(name + "." + field.getName(), fieldValue));
                continue;
            }

            if (fieldValue instanceof VariableReference) {
                VariableReference variableValue = (VariableReference) fieldValue;
                statements.addAll(convert(name + "." + field.getName(), variableValue));
                continue;
            }

            statements.addAll(convert(name, (StructValue) value.getField(field.getName())));
        }
        return statements;
    }

    private static ArrayList<Statement> convertArrayValue(String name, TypeDefinition type, Expression index,
            Expression value) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (type.getType() != VariableType.STRUCT) {
            statements.add(new SimpleArrayAssignment(name, index, value));
            return statements;
        }

        StructValue structValue = (StructValue) value;
        ArrayList<TypeField> fields = type.getFields();
        for (TypeField field : fields) {
            String fieldName = name + "." + field.getName();
            TypeDefinition fieldType = field.getType();
            Expression fieldValue = structValue.getField(field.getName());
            statements.addAll(convertArrayValue(fieldName, fieldType, index, fieldValue));
        }

        return statements;
    }

    private static Expression getArraySize(VariableDeclaration declaration) {
        Expression value = declaration.getExpression();
        if (value instanceof ArrayValue)
            return new IntValue(((ArrayValue) value).getValues().size());
        if (value instanceof ArrayDeclarationValue)
            return ((ArrayDeclarationValue) value).getSize();
        if (value instanceof VariableValue)
            return new SimpleVariableValue("size:" + ((VariableValue) value).getName(),
                    VariableType.INT);

        throw new RuntimeException("Invalid array value: " + value.getClass().getName() + " " + value);
    }

    private static ArrayList<Statement> convertVariableDeclaration(String name, TypeDefinition type, Expression value) {
        ArrayList<Statement> statements = new ArrayList<>();
        ArrayList<TypeField> fields = type.getFields();
        if (type.getType() != VariableType.STRUCT) {
            statements.add(new SimpleVariableDeclaration(name, type.getType()));
            if (value != null)
                statements.add(new SimpleVariableAssignment(name, value));
            return statements;
        }

        if (value == null) {
            for (TypeField field : fields) {
                String fieldName = name + "." + field.getName();
                TypeDefinition fieldType = field.getType();
                statements.addAll(convertVariableDeclaration(fieldName, fieldType, value));
            }
        } else if (value instanceof StructValue) {
            StructValue structValue = (StructValue) value;
            for (TypeField field : fields) {
                String fieldName = name + "." + field.getName();
                TypeDefinition fieldType = field.getType();
                Expression fieldValue = structValue.getField(field.getName());
                statements.addAll(convertVariableDeclaration(fieldName, fieldType, fieldValue));
            }
        } else if (value instanceof VariableValue) {
            VariableValue variableValue = (VariableValue) value;
            for (TypeField field : fields) {
                String variableName = name + "." + field.getName();

                TypeDefinition variableType = field.getType();
                statements.addAll(convertVariableDeclaration(variableName, variableType,
                        new VariableValue(variableValue.getName() + "." + field.getName(), new Type(variableType))));
            }
        }
        return statements;
    }
}
