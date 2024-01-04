package scratch_compiler.Compiler.lexer;

import java.util.ArrayList;

import scratch_compiler.Compiler.StringReader;

public class Lexer {

    public static ArrayList<Token> lex(String input) {
        ArrayList<Token> tokens = new ArrayList<Token>();

        StringReader reader = new StringReader(input);

        while (!reader.isAtEnd()) {
            reader.stripWhitespace();
            if (reader.isAtEnd())
                break;

            Token token = parseSingleToken(reader);

            if (token == null)
                throw new RuntimeException("Invalid token at line " + reader.getLineNumber() + ": " + reader.peek());

            tokens.add(token);
        }

        return tokens;
    }

    private static Token parseSingleToken(StringReader reader) {
        for (TokenType type : TokenType.values()) {
            String remaining = reader.getRemaining();
            int end = type.endOfMatch(remaining);
            if (end == -1)
                continue;
            String match = remaining.substring(0, end);
            reader.strip(match);
            return new Token(type, match, reader.getLineNumber());
        }
        return null;
    }
}
