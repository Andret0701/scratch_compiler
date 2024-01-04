package scratch_compiler.Compiler.parser.nodes;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenType;

public class IntNode extends ValueNode {
    private int value;

    public IntNode(Token token) {
        super(token);
        CompilerUtils.assertIsType(token, TokenType.INT);
        this.value = Integer.parseInt(token.getValue());
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue() + "";
    }
}
