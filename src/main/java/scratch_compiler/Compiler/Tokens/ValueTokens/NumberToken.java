package scratch_compiler.Compiler.Tokens.ValueTokens;

import java.util.ArrayList;

import scratch_compiler.Compiler.Code;
import scratch_compiler.Compiler.SyntaxToken;

public class NumberToken extends SyntaxToken {
    public NumberToken(int position) {
        super(position, "Number", null);
    }

    public String getOpcode() {
        return "Number";
    }

    private String parseNumber(Code code) {
        String number = "";
        boolean foundDecimal = false;
        int offset = 0;
        while (true) {
            char c = code.peek(offset);
            char next = code.peek(offset + 1);
            if (Character.isDigit(c)) {
                number += c;
                offset++;
            } else if (c == '.' && Character.isDigit(next) && !foundDecimal) {
                number += c;
                number += next;
                offset += 2;
                foundDecimal = true;
            } else
                break;
        }
        return number;
    }

    public boolean isNextToken(Code code) {
        String number = parseNumber(code);
        return number.length() > 0;
    }

    public void stripToken(Code code) {
        String number = parseNumber(code);
        setValue(number);
        code.stripToken(number);
    }

    public ArrayList<SyntaxToken> parseSubTokens(Code code) {
        ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
        tokens.add(this);

        // TODO: Parse function parameters

        return tokens;
    }
}