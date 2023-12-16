package scratch_compiler.Compiler.Tokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;

public class BeginScopeToken extends SyntaxToken {
    public BeginScopeToken(int position) {
        super(position, "{", null);
    }

    public String getOpcode() {
        return "BeginScope";
    }

    public boolean isNextToken(Code code) {
        return code.startsWith("{");
    }

    public void stripToken(Code code) {
        code.stripToken("{");
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);
        while (!code.isAtEnd()) {
            ReturnToken returnToken = new ReturnToken(code.getPosition());
            if (returnToken.isNextToken(code)) {
                returnToken.stripToken(code);
                tokens.addAll(returnToken.parseSubTokens(code));
            }

            EndScopeToken endScopeToken = new EndScopeToken(code.getPosition());
            if (endScopeToken.isNextToken(code)) {
                endScopeToken.stripToken(code);
                tokens.addAll(endScopeToken.parseSubTokens(code));
                return tokens;
            }

            NewStatementToken newStatementToken = new NewStatementToken(code.getPosition());
            tokens.addAll(newStatementToken.parseSubTokens(code));
        }

        throw new RuntimeException("Expected '}' at line " + code.getLineNumber());
    }
}
