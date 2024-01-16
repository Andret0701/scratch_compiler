package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.parser.expressions.Expression;
public abstract class ControlFlowStatement extends Statement {
    private Expression expression;
    private Statement statement;

    public ControlFlowStatement(Expression expression, Statement statement) {
        this.statement = statement;
        this.expression = expression;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}