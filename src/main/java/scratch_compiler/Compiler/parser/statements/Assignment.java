package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.Expression;


public class Assignment extends Statement {
    private String name;

    public Assignment(String name, Expression expression) {
        super(TokenType.ASSIGN,expression);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " " + getType() + " " + getExpression();
    }

}
