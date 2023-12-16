package scratch_compiler.Compiler.Tokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;

public class FunctionEndToken extends SyntaxToken {
    public FunctionEndToken(int position) {
        super(position, ")", null);
    }

    public String getOpcode() {
        return "FunctionEnd";
    }

    public boolean isNextToken(Code code) {
        return code.startsWith(")");
    }

    public void stripToken(Code code) {
        code.stripToken(")");
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        BeginScopeToken beginScopeToken = new BeginScopeToken(code.getPosition());
        if (beginScopeToken.isNextToken(code)) {
            beginScopeToken.stripToken(code);
            tokens.addAll(beginScopeToken.parseSubTokens(code));
            return tokens;
        }

        return tokens;
    }
}