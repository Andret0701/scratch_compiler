package scratch_compiler.Compiler.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType {
    WHITESPACE("[ \t\r\n]+", true),
    LINE_COMMENT("//[^\n]*", true),
    BLOCK_COMMENT("/\\*([^*]|\\*+[^*/])*\\*+/", true),

    STRING("\"[^\"]*\""),
    FLOAT("[0-9]+\\.[0-9]+"),
    INT("[0-9]+"),
    BOOLEAN("(true|false)"),

    GREATER_EQUALS(">="),
    LESS_EQUALS("<="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    AND("&&"),
    OR("\\|\\|"),
    NOT_EQUALS("!="),
    NOT("!"),
    EQUALS("=="),

    INCREMENT("\\+\\+"),
    DECREMENT("--"),
    ASSIGN("="),

    SUB("-"),
    ADD("\\+"),
    MUL("\\*"),
    DIV("/"),
    MOD("%"),

    OPEN("\\("),
    CLOSE("\\)"),
    COMMA(","),
    DOT("\\."),
    SIZE("#"),

    SQUARE_BRACKET_OPEN("\\["),
    SQUARE_BRACKET_CLOSE("\\]"),

    BOOLEAN_DECLARATION("bool"),
    FLOAT_DECLARATION("float"),
    INT_DECLARATION("int"),
    STRING_DECLARATION("string"),
    VOID_DECLARATION("void"),
    STRUCT_DECLARATION("struct"),

    IF("if"),
    ELSE("else"),
    WHILE("while"),
    FOR("for"),
    RETURN("return"),

    OPEN_BRACE("\\{"),
    CLOSE_BRACE("\\}"),
    SEMICOLON(";"),

    IDENTIFIER("[a-zA-Z_][a-zA-Z0-9_]*");

    private final Pattern pattern;
    private boolean skip;

    TokenType(String regex) {
        this(regex, false);
    }

    TokenType(String regex, boolean skip) {
        this.pattern = Pattern.compile("^" + regex);
        this.skip = skip;
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find()) {
            return m.end();
        }
        return -1;
    }

    boolean isSkip() {
        return skip;
    }
}