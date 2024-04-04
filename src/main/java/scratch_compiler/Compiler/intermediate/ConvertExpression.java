package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.intermediate.simple_code.VariableReference;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.IndexExpression;
import scratch_compiler.Compiler.parser.expressions.ReferenceExpression;
import scratch_compiler.Compiler.parser.expressions.SizeOfExpression;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertExpression {
    public static void convert(ExpressionContainer container) {
        if (container == null)
            return;

        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression converted = convertExpression(container.getExpression(i));
            convert(converted);
            container.setExpression(i, converted);
        }
    }

    public static Expression convert(Expression expression, IntermediateTable table) {
        if (expression instanceof SizeOfExpression)
            return convertSize((SizeOfExpression) expression);
        if (expression instanceof VariableReference)
            return convertVariableReference((VariableReference) expression);

        for (int i = 0; i < expression.getExpressionCount(); i++) {
            if (expression.getExpression(i) == null)
                continue;
            Expression converted = convert(expression.getExpression(i), table);
            expression.setExpression(i, converted);
        }

        return expression;
    }

    private static Expression convertIndex(IndexExpression indexExpression) {
        Expression expression = indexExpression.getArray();
        if (expression instanceof VariableValue) {
            VariableValue variableValue = (VariableValue) expression;
            return new SimpleArrayValue(variableValue.getName(),
                    variableValue.getType().getType().getType(), indexExpression.getIndex());
        }
        throw new RuntimeException("Invalid index expression: " + indexExpression);
    }

    public static SimpleVariableValue convertSize(SizeOfExpression size) {
        Expression expression = size.getExpression();
        if (expression instanceof VariableReference)
            return new SimpleVariableValue("size:" + ((VariableReference) expression).getName(), VariableType.INT);
        // String name = size.getVariable().getName();
        // return new SimpleVariableValue("size:" + name, VariableType.INT);

        throw new RuntimeException("Invalid size expression: " + size);
        // return new SimpleVariableValue("Fix this", VariableType.INT);
    }

    private static Expression convertReference(ReferenceExpression reference) {
        return convertReference("", reference.getType().getType(), reference);
    }

    private static Expression convertReference(String name, TypeDefinition type, ReferenceExpression reference) {
        Expression expression = reference.getExpression();
        if (expression instanceof VariableReference)
            convertVariableReference((VariableReference) expression);

        throw new RuntimeException("Invalid reference expression: " + reference);
    }

    private static Expression convertVariableValue(VariableValue variableValue) {
        String name = variableValue.getName();

        if (variableValue.getType().isArray() || variableValue.getType().getType().getType() == VariableType.STRUCT)
            throw new IllegalArgumentException("Cannot convert array or struct to variable value: " + variableValue);
        return new SimpleVariableValue(name, variableValue.getType().getType().getType());
    }

    public static Expression convertVariableReference(VariableReference variableReference) {
        if (variableReference.getIndex() == null)
            return new SimpleVariableValue(variableReference.getName(),
                    variableReference.getType().getType().getType());
        return new SimpleArrayValue(variableReference.getName(),
                variableReference.getType().getType().getType(),
                variableReference.getIndex());
    }

    public static ArrayList<IndexExpression> getIndexed(ExpressionContainer expressionContainer) {
        ArrayList<IndexExpression> indexed = new ArrayList<>();
        if (expressionContainer == null)
            return indexed;

        for (int i = 0; i < expressionContainer.getExpressionCount(); i++) {
            Expression expression = expressionContainer.getExpression(i);
            indexed.addAll(getIndexed(expression));
            if (expression instanceof IndexExpression)
                indexed.add((IndexExpression) expression);

        }
        return indexed;
    }

}
