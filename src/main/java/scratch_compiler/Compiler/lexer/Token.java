package scratch_compiler.Compiler.lexer;

public class Token {
    private TokenType type;
    private String value;
    private int line;

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getPrecedence() {
        return type.getPrecedence();
    }

    public boolean isOperator() {
        return getPrecedence() >= 0;
    }

    @Override
    public String toString() {
        return "(" + type + ", '" + value + "')";
    }
}
