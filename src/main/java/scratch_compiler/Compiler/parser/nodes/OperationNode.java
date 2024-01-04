package scratch_compiler.Compiler.parser.nodes;

import scratch_compiler.Compiler.lexer.Token;

public class OperationNode extends Expression {
    public OperationNode(Token token) {
        super(token);
    }

    public int getPrecedence() {
        return token.getPrecedence();
    }
}
