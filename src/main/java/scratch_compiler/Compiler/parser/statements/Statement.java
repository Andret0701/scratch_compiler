package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.ScopeContainer;
import scratch_compiler.Compiler.parser.expressions.Expression;

public abstract class Statement implements ExpressionContainer, ScopeContainer {
    protected ArrayList<Expression> expressions;

    public Statement() {
        expressions = new ArrayList<>();
    }

    public Statement(int numExpressions) {
        expressions = new ArrayList<>();
        for (int i = 0; i < numExpressions; i++)
            expressions.add(null);
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