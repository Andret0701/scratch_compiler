package scratch_compiler.Compiler;

package scratch_compiler.Compiler;

import java.util.ArrayList;

public class TokenCode {
    private ArrayList<SyntaxToken> tokens = new ArrayList<SyntaxToken>();
    private int position;

    public TokenCode(ArrayList<SyntaxToken> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public void next() {
        position++;
    }

    public void next(int offset) {
        position += offset;
    }

    public SyntaxToken peek(int offset) {
        int peekPosition = position + offset;
        if (peekPosition >= tokens.size() || peekPosition < 0)
            return null;
        return tokens.get(peekPosition);
    }

    public SyntaxToken peek() {
        return peek(0);
    }

    public boolean isAtEnd() {
        return position >= tokens.size();
    }

    public boolean startsWith(ArrayList<SyntaxToken> s) {
        for (int i = 0; i < s.size(); i++) // same type
            if (peek(i) == null || !peek(i).getOpcode().equals(s.get(i).getOpcode()))
                return false;
        return true;
    }

    public int getPosition() {
        return position;
    }

    public int getLineNumber() {
        int line = 1;
        for (int i = 0; i < position; i++)
            if (tokens.get(i).getPo
                line++;
        return line;
    }

    public String getLine() {
        int start = position;
        int end = position;
        while (start > 0 && code.charAt(start) != '\n')
            start--;
        while (end < code.length() && code.charAt(end) != '\n')
            end++;
        return code.substring(start, end);
    }

}
