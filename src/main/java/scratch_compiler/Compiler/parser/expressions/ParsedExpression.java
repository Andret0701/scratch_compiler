package scratch_compiler.Compiler.parser.expressions;

import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.parser.expressions.types.ExpressionType;

public class ParsedExpression {
    private Expression expression;
    private Token token;
    private ExpressionType type;
    private Object data;

    public ParsedExpression(Expression expression, Token token, ExpressionType type) {
        this(expression, token, type, null);
    }

    public ParsedExpression(Expression expression, Token token, ExpressionType type, Object data) {
        this.expression = expression;
        this.token = token;
        this.type = type;
        this.data = data;
    }

    public Expression getExpression() {
        return expression;
    }

    public Token getToken() {
        return token;
    }

    public ExpressionType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        String out = ":";
        if (expression == null)
            out += token.toString();
        else
            out += expression.toString();

        out += ", " + type + ":";
        return out;
    }
}
