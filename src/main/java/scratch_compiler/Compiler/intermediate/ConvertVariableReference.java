package scratch_compiler.Compiler.intermediate;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.intermediate.simple_code.VariableReference;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.ScopeContainer;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.IndexExpression;
import scratch_compiler.Compiler.parser.expressions.ReferenceExpression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertVariableReference {
    public static void convert(Statement statement) {
        convert((ExpressionContainer) statement);

        for (int i = 0; i < statement.getScopeCount(); i++) {
            Scope scope = statement.getScope(i);
            convert((ExpressionContainer) scope);
            for (Statement scopeStatement : scope.getStatements()) {
                convert(scopeStatement);
            }
        }
    }

    private static void convert(ExpressionContainer container) {
        if (container == null)
            return;

        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);
            if (expression == null)
                continue;

            Expression converted = convert(expression);
            convert((ExpressionContainer) converted);
            container.setExpression(i, converted);
        }
    }

    private static Expression convert(Expression expression) {

        if (!(expression instanceof ReferenceExpression || expression instanceof IndexExpression
                || expression instanceof VariableValue))
            return expression;

        Expression index = null;
        String reference = "";
        Type type = expression.getType();
        while (expression instanceof ReferenceExpression) {
            ReferenceExpression referenceExpression = (ReferenceExpression) expression;
            reference = "." + referenceExpression.getReference() + reference;
            expression = referenceExpression.getExpression();
        }

        if (expression instanceof IndexExpression) {
            IndexExpression indexExpression = (IndexExpression) expression;
            index = indexExpression.getIndex();
            expression = indexExpression.getArray();
        }

        if (expression instanceof VariableValue) {
            VariableValue variableValue = (VariableValue) expression;
            return new VariableReference(variableValue.getName() + reference, type, index);
        }

        throw new RuntimeException("Invalid variable reference: " + expression);
    }
}
