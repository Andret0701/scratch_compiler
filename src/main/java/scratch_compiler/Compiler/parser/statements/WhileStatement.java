package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.parser.expressions.Expression;

public class WhileStatement extends Statement {
    private Scope scope;

    public WhileStatement(Expression expression, Statement statement) {
        super(1);
        setExpression(0, expression);
        this.scope = new Scope(statement);
    }

    public Expression getExpression() {
        return getExpression(0);
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        String result = "while" + " (" + getExpression() + ") " + getScope();
        return result;
    }

    @Override
    public Scope getScope(int index) {
        if (index == 0)
            return scope;

        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

    @Override
    public void setScope(int index, Scope scope) {
        if (index == 0) {
            this.scope = scope;
            return;
        }
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

    @Override
    public int getScopeCount() {
        return 1;
    }

}