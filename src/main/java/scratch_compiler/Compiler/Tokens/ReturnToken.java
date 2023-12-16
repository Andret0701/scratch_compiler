package scratch_compiler.Compiler.Tokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;
import scratch_compiler.Compiler.Tokens.ValueTokens.ValueToken;

public class ReturnToken extends SyntaxToken {
    public ReturnToken(int position) {
        super(position, "return", null);
    }

    public String getOpcode() {
        return "Return";
    }

    public boolean isNextToken(Code code) {
        return code.startsWith("return");
    }

    public void stripToken(Code code) {
        code.stripToken("return");
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        ValueToken valueToken = new ValueToken(code.getPosition());
        tokens.addAll(valueToken.parseSubTokens(code));

        NewStatementToken newStatementToken = new NewStatementToken(code.getPosition());
        if (newStatementToken.isNextToken(code)) {
            newStatementToken.stripToken(code);
            return tokens;
        }

        throw new RuntimeException("Expected ';' at line " + code.getLineNumber());
    }
}