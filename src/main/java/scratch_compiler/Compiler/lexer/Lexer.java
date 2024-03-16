package scratch_compiler.Compiler.lexer;

import java.util.ArrayList;

import scratch_compiler.Compiler.StringReader;

public class Lexer {

    public static ArrayList<Token> lex(String input) {
        ArrayList<Token> tokens = new ArrayList<Token>();

        StringReader reader = new StringReader(input);

        while (!reader.isAtEnd()) {
            Token token = parseToken(reader);
            if (token.getType().isSkip())
                continue;

            tokens.add(token);
        }

        return tokens;
    }

    private static Token parseToken(StringReader reader) {
        for (TokenType type : TokenType.values()) {
            String remaining = reader.getRemaining();
            int end = type.endOfMatch(remaining);
            if (end == -1)
                continue;
            String match = remaining.substring(0, end);
            reader.strip(match);
            return new Token(type, match, reader.getLineNumber());
        }

        throw new RuntimeException("Invalid token at line " + reader.getLineNumber() + ": " + reader.peek());
    }
}
