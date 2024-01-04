package scratch_compiler.Compiler.parser.nodes;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenType;

public class BinaryOperationNode extends OperationNode {
    private Expression left;
    private Expression right;

    public BinaryOperationNode(Token token) {
        super(token);
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public TokenType getType(IdentifierTypes identifierTypes) {
        return getOperatorType(identifierTypes);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "( " + left + " " + getToken().getType() + " " + right + " )";
    }

    private TokenType getOperatorType(IdentifierTypes identifierTypes) {
        TokenType leftType = left.getType(identifierTypes);
        TokenType rightType = right.getType(identifierTypes);
        if (leftType == null)
            throw new RuntimeException("Unknown identifier '" + left.getToken().getValue()
                    + "' at line " + left.getToken().getLine());
        if (rightType == null)
            throw new RuntimeException("Unknown identifier '" + right.getToken().getValue()
                    + "' at line " + right.getToken().getLine());

        TokenType operatorType = getToken().getType();

        if (operatorType == TokenType.PLUS || operatorType == TokenType.MINUS
                || operatorType == TokenType.MUL
                || operatorType == TokenType.DIV) {
            if (leftType == TokenType.FLOAT && rightType == TokenType.FLOAT)
                return TokenType.FLOAT;
            else if (leftType == TokenType.INT && rightType == TokenType.INT)
                return TokenType.INT;
            else if (leftType == TokenType.FLOAT && rightType == TokenType.INT)
                return TokenType.FLOAT;
            else if (leftType == TokenType.INT && rightType == TokenType.FLOAT)
                return TokenType.FLOAT;
            else
                CompilerUtils.throwOperationNotDefined(operatorType, leftType, rightType);
        } else if (operatorType == TokenType.EQUALS || operatorType == TokenType.NOT_EQUALS) {
            if (leftType == TokenType.FLOAT && rightType == TokenType.FLOAT)
                return TokenType.BOOLEAN;
            else if (leftType == TokenType.INT && rightType == TokenType.INT)
                return TokenType.BOOLEAN;
            else if (leftType == TokenType.FLOAT && rightType == TokenType.INT)
                return TokenType.BOOLEAN;
            else if (leftType == TokenType.INT && rightType == TokenType.FLOAT)
                return TokenType.BOOLEAN;
            else if (leftType == TokenType.BOOLEAN && rightType == TokenType.BOOLEAN)
                return TokenType.BOOLEAN;
            else
                CompilerUtils.throwOperationNotDefined(operatorType, leftType, rightType);
        } else if (operatorType == TokenType.AND || operatorType == TokenType.OR) {
            if (leftType == TokenType.BOOLEAN && rightType == TokenType.BOOLEAN)
                return TokenType.BOOLEAN;
            else
                CompilerUtils.throwOperationNotDefined(operatorType, leftType, rightType);
        }

        throw new RuntimeException(
                "Unknown operator '" + operatorType + "' at line " + token.getLine());
    }
}