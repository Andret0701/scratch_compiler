package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.expressions.Expression;

public class IfStatement extends ControlFlowStatement {
    private Statement elseStatement;

    public IfStatement(Expression expression, Statement statement) {
        super(expression, statement);
    }

    public IfStatement(Expression expression, Statement statement, Statement elseStatement) {
        super(expression, statement);
        this.elseStatement = elseStatement;
    }

    public Statement getElseStatement() {
        return elseStatement;
    }

    @Override
    public String toString() {
        String result = "if" + " (" + getExpression() + ") " + getStatement();
        if (elseStatement != null)
            result += "\nelse " + getElseStatement();

        return result;
    }

    public ArrayList<Statement> getChildren() {
        ArrayList<Statement> children = new ArrayList<Statement>();
        children.add(getStatement());
        if (elseStatement != null)
            children.add(elseStatement);
        return children;
    }

}