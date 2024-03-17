package scratch_compiler.Compiler.optimiser;

import scratch_compiler.Compiler.parser.expressions.AdditionExpression;
import scratch_compiler.Compiler.parser.expressions.BinaryOperationExpression;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.SubtractionExpression;

public class ExpressionOptimiser {
    public static Expression optimise(Expression expression) {
        if (expression instanceof BinaryOperationExpression)
            expression = optimiseBinaryOperationExpression((BinaryOperationExpression) expression);

        return expression;
    }

    private static Expression optimiseBinaryOperationExpression(BinaryOperationExpression expression) {
        expression.setLeft(optimize(expression.getLeft()));
        expression.setRight(optimize(expression.getRight()));
        if (expression instanceof AdditionExpression || expression instanceof SubtractionExpression)
            return AdditionSubtractionOptimiser.optimize(expression);

        return expression;
    }

}
