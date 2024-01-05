package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.parser.expressions.Expression;
public abstract class ControlFlowStatement extends Statement {
    private Expression expression;
    private Statement statement;

    public ControlFlowStatement(Expression expression, Statement statement) {
        this.statement = statement;
        this.expression = expression;
    }

    public Statement getStatement() {
        return statement;
    }

    public Expression getExpression() {
        return expression;
    }
}