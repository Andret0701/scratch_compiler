package scratch_compiler.Compiler.parser.expressions.types;

import scratch_compiler.Compiler.lexer.TokenType;

//https://www.javatpoint.com/operator-precedence-in-c
public enum OperatorType {
    // unary prefix
    UNARY_NEGATION(TokenType.SUB, 3, Associativity.RIGHT_TO_LEFT, true), // -
    NOT(TokenType.NOT, 3, Associativity.RIGHT_TO_LEFT, true), // !

    // binary operators
    MULTIPLICATION(TokenType.MUL, 4, Associativity.LEFT_TO_RIGHT), // *
    DIVISION(TokenType.DIV, 4, Associativity.LEFT_TO_RIGHT), // /
    MODULUS(TokenType.MOD, 4, Associativity.LEFT_TO_RIGHT), // %
    ADDITION(TokenType.ADD, 5, Associativity.LEFT_TO_RIGHT), // +
    SUBTRACTION(TokenType.SUB, 5, Associativity.LEFT_TO_RIGHT), // -

    // relational operators
    LESS_THAN(TokenType.LESS_THAN, 7, Associativity.LEFT_TO_RIGHT), // <
    GREATER_THAN(TokenType.GREATER_THAN, 7, Associativity.LEFT_TO_RIGHT), // >
    LESS_EQUALS(TokenType.LESS_EQUALS, 7, Associativity.LEFT_TO_RIGHT), // <=
    GREATER_EQUALS(TokenType.GREATER_EQUALS, 7, Associativity.LEFT_TO_RIGHT), // >=

    // equality operators
    EQUALS(TokenType.EQUALS, 8, Associativity.LEFT_TO_RIGHT), // ==
    NOT_EQUALS(TokenType.NOT_EQUALS, 8, Associativity.LEFT_TO_RIGHT), // !=

    // logical operators
    AND(TokenType.AND, 12, Associativity.LEFT_TO_RIGHT), // &&
    OR(TokenType.OR, 13, Associativity.LEFT_TO_RIGHT); // ||

    private TokenType tokenType;
    private int precedence;
    private Associativity associativity;
    private boolean isUnary = false;

    OperatorType(TokenType tokenType, int precedence, Associativity associativity) {
        this(tokenType, precedence, associativity, false);
    }

    OperatorType(TokenType tokenType, int precedence, Associativity associativity, boolean isUnary) {
        this.tokenType = tokenType;
        this.precedence = precedence;
        this.associativity = associativity;
        this.isUnary = isUnary;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Associativity getAssociativity() {
        return associativity;
    }

    public boolean isUnary() {
        return isUnary;
    }

    public TokenType getTokenType() {
        return tokenType;
    }
}
