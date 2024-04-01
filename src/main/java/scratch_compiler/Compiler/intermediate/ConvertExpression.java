package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
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
        // if (expression instanceof SizeOfExpression)
        // return convertSize((SizeOfExpression) expression);
        // if (expression instanceof VariableValue)
        // return convertVariableValue((VariableValue) expression);
        return expression;
    }

    public static SimpleVariableValue convertSize(SizeOfExpression size) {
        Expression expression = size.getExpression();
        if (expression instanceof VariableValue)
            return new SimpleVariableValue("size:" + ((VariableValue) expression).getName(), VariableType.INT);
        // String name = size.getVariable().getName();
        // return new SimpleVariableValue("size:" + name, VariableType.INT);

        return new SimpleVariableValue("Fix this", VariableType.INT);
    }

    private static Expression convertReference(ReferenceExpression reference) {
        return new SimpleVariableValue("ref:" + reference.getName(), VariableType.INT);
    }

    private static Expression getReference(String name, ReferenceExpression reference) {
        if (reference instanceof ReferenceExpression)
            return convertReference(name + "." + reference.getReference(), reference);
        if (reference instanceof VariableValue)
            return new Simpl
    }

    private static Expression convertVariableValue(VariableValue variableValue) {
        String name = variableValue.getName();
        TypeReference reference = variableValue.getReference();
        while (reference != null) {
            name = reference.getName() + ":" + name;
            reference = reference.getNext();
        }

        if (variableValue.isArray())
            return new SimpleVariableValue("array:" + name, variableValue.getType().getType());
        return new SimpleVariableValue("var:" + name, variableValue.getType().getType());
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
