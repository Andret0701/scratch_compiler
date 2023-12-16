package scratch_compiler.Compiler.Tokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;

public class EndScopeToken extends SyntaxToken {
    public EndScopeToken(int position) {
        super(position, "}", null);
    }

    public String getOpcode() {
        return "EndScope";
    }

    public boolean isNextToken(Code code) {
        return code.startsWith("}");
    }

    public void stripToken(Code code) {
        code.stripToken("}");
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        NewStatementToken newStatementToken = new NewStatementToken(code.getPosition());
        tokens.addAll(newStatementToken.parseSubTokens(code));

        return tokens;
    }

}