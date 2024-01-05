package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.parser.expressions.Expression;

public class WhileStatement  extends ControlFlowStatement {
    public WhileStatement(Expression expression, Statement statement) {
        super(expression, statement);
    }

    @Override
    public String toString() {
        String result = "while" + " (" + getExpression() + ") " + getStatement();
        return result;
    }

}