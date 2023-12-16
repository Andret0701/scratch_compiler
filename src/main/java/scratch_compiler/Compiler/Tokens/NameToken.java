package scratch_compiler.Compiler.Tokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;

public class NameToken extends SyntaxToken {

    public NameToken(int position) {
        super(position, "Name", null);
    }

    public String getOpcode() {
        return "Name";
    }

    private String parseName(Code code) {
        String name = "";
        int offset = 0;

        while (true) {
            char c = code.peek(offset);
            if (Character.isLetter(c) || (Character.isDigit(c) && offset != 0) || c == '_')
                name += c;
            else
                break;
            offset++;
        }
        return name;
    }

    public boolean isNextToken(Code code) { // This might create a problem because existing syntax tokens are not
                                            // checked for
        String name = parseName(code);
        return name.length() > 0;
    }

    public void stripToken(Code code) {
        String name = parseName(code);
        setValue(name);
        code.stripToken(name);
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        return tokens;
    }

}
