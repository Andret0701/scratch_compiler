package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.parser.expressions.Expression;

public class IfStatement extends Statement {
    private Scope ifScope;
    private Scope elseScope;

    public IfStatement(Expression expression, Statement statement) {
        super(1);
        setExpression(0, expression);
        this.ifScope = new Scope(statement);
    }

    public IfStatement(Expression expression, Statement statement, Statement elseStatement) {
        this(expression, statement);
        this.elseScope = new Scope(elseStatement);
    }

    public Expression getExpression() {
        return getExpression(0);
    }

    public Scope getIfScope() {
        return ifScope;
    }

    public Scope getElseScope() {
        return elseScope;
    }

    @Override
    public String toString() {
        String result = "if" + " (" + getExpression() + ") " + getIfScope();
        if (getElseScope() != null)
            result += "\nelse " + getElseScope();

        return result;
    }

    @Override
    public Scope getScope(int index) {
        if (index == 0)
            return ifScope;
        else if (index == 1 && this.elseScope != null)
            return elseScope;

        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

    @Override
    public void setScope(int index, Scope scope) {
        if (index == 0) {
            this.ifScope = scope;
            return;
        } else if (index == 1 && this.elseScope != null) {
            this.elseScope = scope;
            return;
        }
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

    @Override
    public int getScopeCount() {
        if (this.elseScope == null)
            return 1;
        return 2;
    }

}