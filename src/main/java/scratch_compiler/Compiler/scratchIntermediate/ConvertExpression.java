package scratch_compiler.Compiler.scratchIntermediate;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;

public class ConvertExpression {

    public static void convert(ExpressionContainer container) {
        if (container == null)
            return;

        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);
            if (expression == null)
                continue;

            container.setExpression(i, convert(expression));
            convert((ExpressionContainer) expression);
        }
    }

    public static Expression convert(Expression expression) {
        if (expression instanceof SimpleArrayValue) {
            SimpleArrayValue arrayValue = (SimpleArrayValue) expression;
            arrayValue.setIndex(new BinaryOperator(OperatorType.ADDITION, arrayValue.getIndex(), new IntValue(1),
                    new Type(VariableType.INT)));
        }
        return expression;
    }
}
