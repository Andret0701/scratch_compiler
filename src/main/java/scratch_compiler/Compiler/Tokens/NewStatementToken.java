package scratch_compiler.Compiler.Tokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;

public class NewStatementToken extends SyntaxToken {
    public NewStatementToken(int position) {
        super(position, ";", null);
    }

    public String getOpcode() {
        return "NewStatement";
    }

    public boolean isNextToken(Code code) {
        return code.startsWith(";");
    }

    public void stripToken(Code code) {
        code.stripToken(";");
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        NumberTypeToken numberTypeToken = new NumberTypeToken(code.getPosition());
        if (numberTypeToken.isNextToken(code)) {
            numberTypeToken.stripToken(code);
            tokens.addAll(numberTypeToken.parseSubTokens(code));
            return tokens;
        }

        return tokens;
    }

}