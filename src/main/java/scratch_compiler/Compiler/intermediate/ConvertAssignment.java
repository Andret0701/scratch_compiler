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
            statements.addAll(convert(variable, structValue));
        } else if (expression instanceof VariableReference) {
            VariableReference value = (VariableReference) expression;
            statements.addAll(convert(variable, value));
        } else {
            Expression index = variable.getIndex();
            if (index != null) {
                statements.add(new SimpleArrayAssignment(variable.getName(), index, expression));
                return statements;
            }
            statements.add(new SimpleVariableAssignment(variable.getName(), expression));
        }

        return statements;
    }

    private static ArrayList<Statement> convert(VariableReference variable, VariableReference value) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (value.getType().getType().getType() != VariableType.STRUCT) {
            if (variable.getIndex() == null)
                statements.add(new SimpleVariableAssignment(variable.getName(),
                        ConvertExpression.convertVariableReference(value)));
            else
                statements.add(new SimpleArrayAssignment(variable.getName(), variable.getIndex(),
                        ConvertExpression.convertVariableReference(value)));

            return statements;
        }

        for (TypeField field : value.getType().getType().getFields()) {
            String fieldName = variable.getName() + "." + field.getName();
            TypeDefinition fieldType = field.getType();
            statements.addAll(convert(new VariableReference(fieldName, new Type(fieldType), variable.getIndex()),
                    new VariableReference(value.getName() + "." + field.getName(), new Type(fieldType),
                            value.getIndex())));
        }

        return statements;
    }

    private static ArrayList<Statement> convert(VariableReference variable, StructValue value) {
        ArrayList<Statement> statements = new ArrayList<>();

        for (TypeField field : value.getType().getType().getFields()) {
            TypeDefinition fieldType = field.getType();
            Expression fieldValue = value.getField(field.getName());
            String name = variable.getName() + "." + field.getName();
            if (fieldType.getType() != VariableType.STRUCT) {

                if (variable.getIndex() == null)
                    statements.add(new SimpleVariableAssignment(name, fieldValue));
                else
                    statements.add(new SimpleArrayAssignment(name, variable.getIndex(), fieldValue));
                continue;
            }

            if (fieldValue instanceof VariableReference) {
                VariableReference variableValue = (VariableReference) fieldValue;
                statements.addAll(convert(new VariableReference(name, new Type(fieldType), variable.getIndex()),
                        variableValue));
                continue;
            }

            statements.addAll(convert(new VariableReference(name, variable.getType(), variable.getIndex()),
                    (StructValue) value.getField(field.getName())));
        }
        return statements;
    }

}
