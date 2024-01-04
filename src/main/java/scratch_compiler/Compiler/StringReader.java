package scratch_compiler.Compiler;

public class StringReader {
    private String string;
    private int position;

    public StringReader(String string) {
        this.string = string;
        this.position = 0;
    }

    public void next() {
        position++;
    }

    public char peek(int offset) {
        int peekPosition = position + offset;
        if (peekPosition >= string.length() || peekPosition < 0)
            return '\0';
        return string.charAt(peekPosition);
    }

    public char peek() {
        return peek(0);
    }

    public boolean isAtEnd() {
        return position >= string.length();
    }

    public boolean startsWith(String s) {
        return string.substring(position).startsWith(s);
    }

    public void stripWhitespace() {
        while (position < string.length() && Character.isWhitespace(string.charAt(position)))
            position++;
    }

    public void strip(String string) {
        if (startsWith(string))
            position += string.length();
    }

    public int getPosition() {
        return position;
    }

    public int getLineNumber() {
        int line = 1;
        for (int i = 0; i < position; i++)
            if (string.charAt(i) == '\n')
                line++;
        return line;
    }

    public String getLine() {
        int start = position;
        int end = position;
        while (start > 0 && string.charAt(start) != '\n')
            start--;
        while (end < string.length() && string.charAt(end) != '\n')
            end++;
        return string.substring(start, end);
    }

    public String getRemaining() {
        return string.substring(position);
    }

}
