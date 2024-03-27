package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.VariableReference;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.SizeOfExpression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;

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

    private static Expression convertExpression(Expression expression) {
        if (expression instanceof SizeOfExpression)
            return convertSize((SizeOfExpression) expression);
        if (expression instanceof VariableValue)
            return convertVariableValue((VariableValue) expression);
        return expression;
    }

    public static SimpleVariableValue convertSize(SizeOfExpression size) {
        String name = size.getVariable().getName();
        return new SimpleVariableValue("size:" + name, VariableType.INT);
    }

    private static Expression convertVariableValue(VariableValue variableValue) {
        String name = variableValue.getName();
        VariableReference reference = variableValue.getReference();
        while (reference != null) {
            name = reference.getName() + ":" + name;
            reference = reference.getNext();
        }

        if (variableValue.isArray())
            return new SimpleVariableValue("array:" + name, variableValue.getType().getType());
        return new SimpleVariableValue("var:" + name, variableValue.getType().getType());
    }

    public static ArrayList<VariableValue> getIndexed(ExpressionContainer expressionContainer) {
        ArrayList<VariableValue> indexed = new ArrayList<>();
        if (expressionContainer == null)
            return indexed;

        for (int i = 0; i < expressionContainer.getExpressionCount(); i++) {
            Expression expression = expressionContainer.getExpression(i);
            indexed.addAll(getIndexed(expression));
            if (expression instanceof VariableValue) {
                VariableValue variableValue = (VariableValue) expression;
                if (variableValue.isArray())
                    indexed.add(variableValue);
            }
        }
        return indexed;
    }
}
