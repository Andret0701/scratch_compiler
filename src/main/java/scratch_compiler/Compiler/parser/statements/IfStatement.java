package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.Expression;

public class IfStatement extends ControlFlowStatement {
    private Statement elseStatement;
    public IfStatement(Expression expression, Statement statement) {
        super(TokenType.IF, expression, statement);
    }

    public IfStatement(Expression expression, Statement statement, Statement elseStatement) {
        super(TokenType.IF, expression, statement);
        this.elseStatement = elseStatement;
    }

    public Statement getElseStatement() {
        return elseStatement;
    }

    @Override
    public String toString() {
        String result = "if" + " (" + getExpression() + ") " + getStatement();
        if (elseStatement != null) 
            result += "\nelse " + elseStatement;
        
        return result;
    }

}