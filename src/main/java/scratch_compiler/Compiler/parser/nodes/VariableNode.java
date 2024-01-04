package scratch_compiler.Compiler.parser.nodes;

import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenType;

public class VariableNode extends ValueNode {
    private String name;

    public VariableNode(String name) {
        super(new Token(TokenType.IDENTIFIER, name, -1));
        this.name = name;
    }

    public VariableNode(Token token) {
        super(token);
        this.name = token.getValue();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
