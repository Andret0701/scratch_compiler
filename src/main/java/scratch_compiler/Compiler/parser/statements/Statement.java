package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.Expression;

public class Statement {
    private TokenType type;
    private Expression expression;

    public Statement(TokenType type, Expression expression) {
        this.type = type;
        this.expression = expression;
    }

    public TokenType getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return type + " " + expression;
    }

}
