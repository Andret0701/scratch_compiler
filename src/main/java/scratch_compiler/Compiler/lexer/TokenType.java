package scratch_compiler.Compiler.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType {
    AND("&&", 3),
    OR("\\|\\|", 1),
    EQUALS("==", 8),
    NOT_EQUALS("!=", 8),
    ADD_ASSIGN("\\+="),
    SUB_ASSIGN("-="),
    MUL_ASSIGN("\\*="),
    DIV_ASSIGN("/="),
    ASSIGN("="),
    MINUS("-", 13),
    PLUS("\\+", 13),
    MUL("\\*", 14),
    DIV("/", 14),
    OPEN("\\("),
    CLOSE("\\)"),
    COMMA(","),
    BOOLEAN_DECLARATION("bool"),
    BOOLEAN("(true|false)"),
    FLOAT_DECLARATION("float"),
    FLOAT("[0-9]+\\.[0-9]+"),
    INT_DECLARATION("int"),
    INT("[0-9]+"),
    IF("if"),
    ELSE("else"),
    OPEN_BRACE("\\{"),
    CLOSE_BRACE("\\}"),
    SEMICOLON(";"),
    IDENTIFIER("[a-zA-Z_][a-zA-Z0-9_]*");

    private final Pattern pattern;
    private final int precedence;

    TokenType(String regex, int precedence) {
        this.pattern = Pattern.compile("^" + regex);
        this.precedence = precedence;
    }

    TokenType(String regex) {
        this(regex, -1);
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find()) {
            return m.end();
        }
        return -1;
    }

    int getPrecedence() {
        return precedence;
    }
}