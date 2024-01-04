package scratch_compiler.Compiler.parser.nodes;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenType;

public class FloatNode extends ValueNode {
    private double value;

    public FloatNode(Token token) {
        super(token);
        CompilerUtils.assertIsType(token, TokenType.FLOAT);
        this.value = Double.parseDouble(token.getValue());
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue() + "";
    }
}
