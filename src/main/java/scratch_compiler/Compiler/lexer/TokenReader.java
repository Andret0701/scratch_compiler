package scratch_compiler.Compiler.lexer;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompilerUtils;

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

    public Token expectNext(TokenType... types) {
        for (TokenType type : types)
            if (isNext(type))
                return pop();

        String expected = "";
        for (int i = 0; i < types.length; i++)
        {
            expected += types[i];
            if (i != types.length - 2)
                expected += " or ";
            else if (i == types.length - 2)
                expected += ", ";
        }
        CompilerUtils.throwExpected(expected, peek().getLine(), peek());
        return null;
    }

    public  boolean isNext(TokenType type) {
        if (peek()==null||peek().getType() != type)
            return false;
        return true;
    }

    public boolean isAt(int offset, TokenType type) {
        Token token = peek(offset);
        if (token == null)
            return false;
        return token.getType() == type;
    }

    @Override
    public String toString() {
        String result = "";
        for (Token token : tokens)
            result += token + " ";
        return result;
    }
}
