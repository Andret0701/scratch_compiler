package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.TypeField;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.ArrayDeclarationValue;
import scratch_compiler.Compiler.parser.expressions.values.ArrayValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StructValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ConvertDeclaration {
    public static ArrayList<Statement> declareVariable(String name, TypeDefinition type) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (type.getType() != VariableType.STRUCT) {
            statements.add(new SimpleVariableDeclaration(name, type.getType()));
            return statements;
        }

        for (TypeField field : type.getFields()) {
            String fieldName = name + ":" + field.getName();
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
            String fieldName = name + ":" + field.getName();
            statements.addAll(declareArray(fieldName, size, field.getType()));
        }

        return statements;
    }

    public static ArrayList<Statement> convert(VariableDeclaration declaration) {
        Type type = declaration.getVariable().getType();
        String name = declaration.getVariable().getName();
        Expression value = declaration.getExpression();

        ArrayList<Statement> statements = new ArrayList<>();
        if (declaration.getVariable().getType().isArray()) {
            statements.add(new SimpleVariableDeclaration("size:" + name, VariableType.INT));
            statements.add(new SimpleVariableAssignment("size:" + name, getArraySize(declaration)));
            statements.addAll(
                    convertArrayDeclaration("array:" + name, type.getType(),
                            new SimpleVariableValue("size:" + name, VariableType.INT)));

            if (value instanceof ArrayValue)
                statements.addAll(convertArrayValue(name, type.getType(), (ArrayValue) value));

            return statements;
        }

        statements.addAll(convertVariableDeclaration("var:" + name, type.getType(), value));
        return statements;
    }

    private static ArrayList<Statement> convertArrayDeclaration(String name, TypeDefinition type, Expression size) {
        ArrayList<Statement> statements = new ArrayList<Statement>();
        if (type.getType() != VariableType.STRUCT) {
            SimpleArrayDeclaration arrayDeclaration = new SimpleArrayDeclaration(name, type.getType(), size);
            statements.add(arrayDeclaration);
            return statements;
        }

        ArrayList<TypeField> fields = type.getFields();
        for (TypeField field : fields) {
            String fieldName = name + ":" + field.getName();
            TypeDefinition fieldType = field.getType();
            statements.addAll(convertArrayDeclaration(fieldName, fieldType, size));
        }

        return statements;
    }

    private static ArrayList<Statement> convertArrayValue(String name, TypeDefinition type, ArrayValue value) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (int i = 0; i < value.getValues().size(); i++) {
            Expression element = value.getValues().get(i);
            statements.addAll(convertArrayValue(name + ":" + i, type, new IntValue(i), element));
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
            String fieldName = name + ":" + field.getName();
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

        throw new RuntimeException("Invalid array value");
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
                String fieldName = name + ":" + field.getName();
                TypeDefinition fieldType = field.getType();
                statements.addAll(convertVariableDeclaration(fieldName, fieldType, value));
            }
        } else if (value instanceof StructValue) {
            StructValue structValue = (StructValue) value;
            for (TypeField field : fields) {
                String fieldName = name + ":" + field.getName();
                TypeDefinition fieldType = field.getType();
                Expression fieldValue = structValue.getField(field.getName());
                statements.addAll(convertVariableDeclaration(fieldName, fieldType, fieldValue));
            }
        } else if (value instanceof VariableValue) {
            VariableValue variableValue = (VariableValue) value;
            for (TypeField field : fields) {
                String variableName = name + ":" + field.getName();
                TypeDefinition variableType = field.getType();
                statements.addAll(convertVariableDeclaration(variableName, variableType,
                        new VariableValue(variableValue.getName(), new Type(variableType))));
            }
        }
        return statements;
    }
}
