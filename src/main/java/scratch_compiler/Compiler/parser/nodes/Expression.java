package scratch_compiler.Compiler.parser.nodes;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenType;

public class Expression {
    protected Token token;

    public Expression(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public TokenType getType(IdentifierTypes identifierTypes) {
        if (token.getType() == TokenType.IDENTIFIER)
            return identifierTypes.get(token.getValue());
        return token.getType();
    }

    @Override
    public String toString() {
        return token.toString();
    }
}
