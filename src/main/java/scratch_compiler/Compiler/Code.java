package scratch_compiler.Compiler;

public class Code {
    private String code;
    private int position;

    public Code(String code) {
        this.code = code;
        this.position = 0;
    }

    public void next() {
        position++;
    }

    public char peek(int offset) {
        int peekPosition = position + offset;
        if (peekPosition >= code.length() || peekPosition < 0)
            return '\0';
        return code.charAt(peekPosition);
    }

    public char peek() {
        return peek(0);
    }

    public boolean isAtEnd() {
        return position >= code.length();
    }

    public boolean startsWith(String s) {
        return code.substring(position).startsWith(s);
    }

    public void stripWhitespace() {
        while (position < code.length() && Character.isWhitespace(code.charAt(position)))
            position++;
    }

    public void stripToken(String token) {
        if (startsWith(token))
            position += token.length();

        stripWhitespace();
    }

    public int getPosition() {
        return position;
    }

    public int getLineNumber() {
        int line = 1;
        for (int i = 0; i < position; i++)
            if (code.charAt(i) == '\n')
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
