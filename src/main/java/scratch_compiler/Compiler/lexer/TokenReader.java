package scratch_compiler.Compiler.lexer;

import java.util.ArrayList;

public class TokenReader {
    private ArrayList<Token> tokens;
    private int position;

    public TokenReader(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public Token pop() {
        Token token = peek();
        next();
        return token;
    }

    public void next() {
        position++;
    }

    public Token peek(int offset) {
        int peekPosition = position + offset;
        if (peekPosition >= tokens.size() || peekPosition < 0)
            return null;
        return tokens.get(peekPosition);
    }

    public Token peek() {
        return peek(0);
    }

    public boolean isAtEnd() {
        return position >= tokens.size();
    }

    public int getPosition() {
        return position;
    }

    public int size() {
        return tokens.size();
    }

    public void expect(TokenType type) {
        if (!isNext(type))
            throw new RuntimeException("Expected " + type + " at line " + peek().getLine());
    }

    public  boolean isNext(TokenType type) {
        if (peek()==null||peek().getType() != type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        String result = "";
        for (Token token : tokens)
            result += token + " ";
        return result;
    }
}
