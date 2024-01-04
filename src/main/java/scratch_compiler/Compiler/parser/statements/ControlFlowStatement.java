package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.Expression;
public class ControlFlowStatement extends Statement {
    private Statement statement;

    public ControlFlowStatement(TokenType type, Expression expression, Statement statement) {
        super(type, expression);
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public String toString() {
        return getType() + " (" + getExpression() + ") " + statement;
    }
}