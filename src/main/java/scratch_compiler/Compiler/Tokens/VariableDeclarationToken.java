package scratch_compiler.Compiler.Tokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;
import scratch_compiler.Compiler.Tokens.ValueTokens.ValueToken;

public class VariableDeclarationToken extends SyntaxToken {
    public VariableDeclarationToken(int position) {
        super(position, "=", null);
    }

    public String getOpcode() {
        return "VariableDeclaration";
    }

    public boolean isNextToken(Code code) {
        return code.startsWith("=");
    }

    public void stripToken(Code code) {
        code.stripToken("=");
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        // TODO: Parse function parameters
        ValueToken valueToken = new ValueToken(code.getPosition());
        tokens.addAll(valueToken.parseSubTokens(code));

        NewStatementToken newStatementToken = new NewStatementToken(code.getPosition());
        if (newStatementToken.isNextToken(code)) {
            newStatementToken.stripToken(code);
            tokens.addAll(newStatementToken.parseSubTokens(code));
            return tokens;
        }

        throw new RuntimeException("Expected ';' at line " + code.getLineNumber());
    }
}
