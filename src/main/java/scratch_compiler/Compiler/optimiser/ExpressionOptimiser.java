package scratch_compiler.Compiler.optimiser;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.AdditionExpression;
import scratch_compiler.Compiler.parser.expressions.BinaryOperationExpression;
import scratch_compiler.Compiler.parser.expressions.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.FloatValue;
import scratch_compiler.Compiler.parser.expressions.IntValue;
import scratch_compiler.Compiler.parser.expressions.SubtractionExpression;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.ValueFields.UnaryOperationField;

public class ExpressionOptimiser {
    public static Expression optimise(Expression expression) {
        if (expression instanceof BinaryOperationExpression)
            expression = optimiseBinaryOperationExpression((BinaryOperationExpression) expression);
              

        return expression;
    }

    private static Expression optimiseBinaryOperationExpression(BinaryOperationExpression expression) {
        expression.setLeft(optimise(expression.getLeft()));
        expression.setRight(optimise(expression.getRight()));
        if (expression instanceof AdditionExpression || expression instanceof SubtractionExpression)
            return AdditionSubtractionOptimiser.optimise(expression);

        return expression;
    }






}
