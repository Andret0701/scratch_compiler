package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.Expression;
public class Declaration extends Statement{
    private String name;

    public Declaration(String name,TokenType type, Expression expression) {
        super(type,expression);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getType() + " " + name + " = " + getExpression();
    }
}