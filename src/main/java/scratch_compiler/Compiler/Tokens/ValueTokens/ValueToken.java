package scratch_compiler.Compiler.Tokens.ValueTokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;

public class ValueToken extends SyntaxToken {
    public ValueToken(int position) {
        super(position, "", null);
    }

    public String getOpcode() {
        return "Value";
    }

    public boolean isNextToken(Code code) {
        return true;
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        NumberToken numberToken = new NumberToken(code.getPosition());
        if (numberToken.isNextToken(code)) {
            numberToken.stripToken(code);
            tokens.addAll(numberToken.parseSubTokens(code));
            tokens.addAll(parseSubOperationTokens(code));
            return tokens;
        }

        throw new RuntimeException("Unknown value type");
    }

    private ArrayList<SyntaxToken> parseSubOperationTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        return tokens;
    }
}