package scratch_compiler.Compiler.parser.expressions;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.ExpressionContainer;

public abstract class Expression implements ExpressionContainer {
    protected ArrayList<Expression> expressions;;

    public Expression() {
        expressions = new ArrayList<Expression>();
    }

    public Expression(int numExpressions) {
        expressions = new ArrayList<Expression>();
        for (int i = 0; i < numExpressions; i++) {
            expressions.add(null);
        }
    }

    public abstract Type getType();

    public boolean isConstant() {
        return false;
    }

    @Override
    public Expression getExpression(int index) {
        return expressions.get(index);
    }

    @Override
    public void setExpression(int index, Expression expression) {
        expressions.set(index, expression);
    }

    @Override
    public int getExpressionCount() {
        return expressions.size();
    }
}
