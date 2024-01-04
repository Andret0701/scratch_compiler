package scratch_compiler.Compiler.parser.nodes;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenType;

public class BooleanNode extends ValueNode {
    private boolean value;

    public BooleanNode(Token token) {
        super(token);
        CompilerUtils.assertIsType(token, TokenType.BOOLEAN);
        this.value = Boolean.parseBoolean(token.getValue());
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue() + "";
    }
}
