package scratch_compiler.Compiler.parser.nodes;

import scratch_compiler.Compiler.lexer.Token;

public class ValueNode extends Expression {
    public ValueNode(Token token) {
        super(token);
    }
}