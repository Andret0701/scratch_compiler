package scratch_compiler.Compiler.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType {
    STRING("\"[^\"]*\"", TokenSubtype.VALUE),
    FLOAT("[0-9]+\\\\.[0-9]+", TokenSubtype.VALUE),
    INT("[0-9]+", TokenSubtype.VALUE),
    BOOLEAN("(true|false)", TokenSubtype.VALUE),

    GREATER_EQUALS(">=", TokenSubtype.BINARY_OPERATOR),
    LESS_EQUALS("<=", TokenSubtype.BINARY_OPERATOR),
    LESS_THAN("<", TokenSubtype.BINARY_OPERATOR),
    GREATER_THAN(">", TokenSubtype.BINARY_OPERATOR),
    AND("&&", TokenSubtype.BINARY_OPERATOR),
    OR("\\|\\|", TokenSubtype.BINARY_OPERATOR),
    EQUALS("==", TokenSubtype.BINARY_OPERATOR),
    NOT_EQUALS("!=", TokenSubtype.BINARY_OPERATOR),
    
    INCREMENT("\\+\\+", TokenSubtype.ASSIGNMENT),
    DECREMENT("--", TokenSubtype.ASSIGNMENT),
    ADD_ASSIGN("\\+=", TokenSubtype.ASSIGNMENT),
    SUB_ASSIGN("-=", TokenSubtype.ASSIGNMENT),
    MUL_ASSIGN("\\*=", TokenSubtype.ASSIGNMENT),
    DIV_ASSIGN("/=", TokenSubtype.ASSIGNMENT),
    MOD_ASSIGN("%=", TokenSubtype.ASSIGNMENT),
    ASSIGN("=", TokenSubtype.ASSIGNMENT),
    
    SUB("-", TokenSubtype.BINARY_OPERATOR),
    ADD("\\+", TokenSubtype.BINARY_OPERATOR),
    MUL("\\*", TokenSubtype.BINARY_OPERATOR),
    DIV("/", TokenSubtype.BINARY_OPERATOR),
    MOD("%", TokenSubtype.BINARY_OPERATOR),
    
    OPEN("\\("),
    CLOSE("\\)"),
    COMMA(","),
    
    
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
    
    OPEN_BRACE("\\{"),
    CLOSE_BRACE("\\}"),
    SEMICOLON(";"),

    IDENTIFIER("[a-zA-Z_][a-zA-Z0-9_]*");

    private final Pattern pattern;
    private final TokenSubtype subtype;

    TokenType(String regex, TokenSubtype subtype) {
        this.pattern = Pattern.compile("^" + regex);
        this.subtype = subtype;
    }

    TokenType(String regex) {
        this(regex, TokenSubtype.KEYWORD);
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find()) {
            return m.end();
        }
        return -1;
    }

    public TokenSubtype getSubtype() {
        return subtype;
    }
}