package scratch_compiler.Compiler.parser;

import java.util.ArrayList;
import java.util.List;

import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.FunctionCallExpression;
import scratch_compiler.Compiler.parser.expressions.OperatorExpression;
import scratch_compiler.Compiler.parser.expressions.ParsedExpression;
import scratch_compiler.Compiler.parser.expressions.TypeConversionExpression;
import scratch_compiler.Compiler.parser.expressions.UnaryOperator;
import scratch_compiler.Compiler.parser.expressions.types.Associativity;
import scratch_compiler.Compiler.parser.expressions.types.ExpressionType;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StringValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.UnaryOperatorDefinition;

public class ExpressionParser {
    public static Expression parse(Type type, TokenReader tokens, DeclarationTable declarationTable) {
        if (tokens.isNext(TokenType.OPEN_BRACE))
            return StructParser.parse(type, tokens, declarationTable);

        Expression expression = parse(tokens, declarationTable);
        if (expression == null)
            return null;

        declarationTable.validateTypeConversion(expression.getType(), type, tokens.peek().getLine());
        return declarationTable.convertExpression(expression, type);
    }

    public static Expression parse(TokenReader tokens, DeclarationTable declarationTable) {
        ArrayList<ParsedExpression> expressions = parseExpressions(tokens, declarationTable);
        Expression expression = assembleExpression(expressions, declarationTable);
        return expression;
    }

    private static OperatorType getOperatorType(TokenType operatorType, boolean isUnary) {
        for (OperatorType operator : OperatorType.values()) {
            if (operator.getTokenType() == operatorType && operator.isUnary() == isUnary)
                return operator;
        }
        throw new RuntimeException("Invalid operator " + operatorType);
    }

    private static int getUnaryOperatorPrecedence(TokenType operatorType) {
        for (OperatorType operator : OperatorType.values()) {
            if (operator.getTokenType() == operatorType && operator.isUnary())
                return operator.getPrecedence();
        }
        throw new RuntimeException("Invalid operator " + operatorType);
    }

    private static Associativity getUnaryOperatorAssociativity(TokenType operatorType) {
        for (OperatorType operator : OperatorType.values()) {
            if (operator.getTokenType() == operatorType && operator.isUnary())
                return operator.getAssociativity();
        }
        throw new RuntimeException("Invalid operator " + operatorType);
    }

    private static int getBinaryOperatorPrecedence(TokenType operatorType) {
        for (OperatorType operator : OperatorType.values()) {
            if (operator.getTokenType() == operatorType && !operator.isUnary())
                return operator.getPrecedence();
        }
        throw new RuntimeException("Invalid operator " + operatorType);
    }

    private static Associativity getBinaryOperatorAssociativity(TokenType operatorType) {
        for (OperatorType operator : OperatorType.values()) {
            if (operator.getTokenType() == operatorType && !operator.isUnary())
                return operator.getAssociativity();
        }
        throw new RuntimeException("Invalid operator " + operatorType);
    }

    private static boolean nextIsUnaryOperator(TokenReader tokens) {
        TokenType type = tokens.peek().getType();
        for (OperatorType operator : OperatorType.values()) {
            TokenType tokenType = operator.getTokenType();
            if (tokenType == type && operator.isUnary())
                return true;
        }
        return false;
    }

    private static ParsedExpression parseUnaryOperator(TokenReader tokens) {
        if (!nextIsUnaryOperator(tokens))
            CompilerUtils.throwExpected("unary operator", tokens.peek().getLine(), tokens.peek());

        Token unaryToken = tokens.pop();
        return new ParsedExpression(null, unaryToken, ExpressionType.UNARY_OPERATION);
    }

    public static boolean nextIsBinaryOperator(TokenReader tokens) {
        TokenType type = tokens.peek().getType();
        for (OperatorType operator : OperatorType.values()) {
            TokenType tokenType = operator.getTokenType();
            if (tokenType == type && !operator.isUnary())
                return true;
        }
        return false;
    }

    public static OperatorType getBinaryOperatorType(Token token) {
        for (OperatorType operator : OperatorType.values()) {
            TokenType tokenType = operator.getTokenType();
            if (tokenType == token.getType() && !operator.isUnary())
                return operator;
        }
        throw new RuntimeException("Invalid operator " + token);
    }

    private static ParsedExpression parseBinaryOperator(TokenReader tokens) {
        if (!nextIsBinaryOperator(tokens))
            CompilerUtils.throwExpected("binary operator", tokens.peek().getLine(), tokens.peek());
        Token binaryToken = tokens.pop();
        return new ParsedExpression(null, binaryToken, ExpressionType.BINARY_OPERATION);
    }

    private static boolean nextIsFunctionCall(TokenReader tokens) {
        return tokens.isAt(0, TokenType.IDENTIFIER) && tokens.isAt(1, TokenType.OPEN);
    }

    private static ParsedExpression parseFunctionCall(TokenReader tokens, DeclarationTable declarationTable) {
        if (!nextIsFunctionCall(tokens))
            CompilerUtils.throwExpected("function call", tokens.peek().getLine(), tokens.peek());

        Token functionToken = tokens.expectNext(TokenType.IDENTIFIER);

        String functionName = functionToken.getValue();
        declarationTable.validateFunctionUsage(functionName, tokens.peek().getLine());
        Type returnType = declarationTable.getFunction(functionName).getReturnType();

        tokens.expectNext(TokenType.OPEN);
        ArrayList<Expression> arguments = new ArrayList<Expression>();
        while (tokens.peek().getType() != TokenType.CLOSE) {
            arguments.add(parse(tokens, declarationTable));
            if (tokens.peek().getType() == TokenType.COMMA)
                tokens.pop();
            else if (tokens.peek().getType() != TokenType.CLOSE)
                CompilerUtils.throwExpected("comma or close parenthesis", tokens.peek().getLine(), tokens.peek());
        }
        tokens.expectNext(TokenType.CLOSE);

        FunctionCallExpression functionCall = new FunctionCallExpression(functionName, arguments, returnType);

        return new ParsedExpression(functionCall, functionToken, ExpressionType.VALUE);
    }

    private static boolean nextIsVariable(TokenReader tokens) {
        TokenType type = tokens.peek().getType();
        return type == TokenType.IDENTIFIER && !nextIsFunctionCall(tokens);
    }

    private static ParsedExpression parseVariable(TokenReader tokens, DeclarationTable declarationTable) {
        if (!nextIsVariable(tokens))
            CompilerUtils.throwExpected("variable", tokens.peek().getLine(), tokens.peek());

        Token variableToken = tokens.peek();
        VariableValue variable = VariableParser.parse(tokens, declarationTable);
        return new ParsedExpression(variable, variableToken, ExpressionType.VALUE);
    }

    private static boolean nextIsValue(TokenReader tokens) {
        TokenType type = tokens.peek().getType();
        return type == TokenType.INT || type == TokenType.FLOAT || type == TokenType.BOOLEAN
                || type == TokenType.STRING;
    }

    private static ParsedExpression parseValue(TokenReader tokens, DeclarationTable declarationTable) {
        if (!nextIsValue(tokens))
            CompilerUtils.throwExpected("value", tokens.peek().getLine(), tokens.peek());

        Token valueToken = tokens.pop();
        String value = valueToken.getValue();
        Expression valueExpression = null;
        switch (valueToken.getType()) {
            case INT:
                valueExpression = new IntValue(Integer.parseInt(value));
                break;
            case FLOAT:
                valueExpression = new FloatValue(Double.parseDouble(value));
                break;
            case BOOLEAN:
                valueExpression = new BooleanValue(Boolean.parseBoolean(value));
                break;
            case STRING:
                valueExpression = new StringValue(value.substring(1, value.length() - 1));
                break;
            default:
                CompilerUtils.throwExpected("value", valueToken.getLine(), valueToken);
        }

        return new ParsedExpression(valueExpression, valueToken, ExpressionType.VALUE);
    }

    private static boolean nextIsOpen(TokenReader tokens) {
        TokenType type = tokens.peek().getType();
        return type == TokenType.OPEN;
    }

    private static ParsedExpression parseOpen(TokenReader tokens, DeclarationTable declarationTable) {
        if (!nextIsOpen(tokens))
            CompilerUtils.throwExpected("open parenthesis", tokens.peek().getLine(), tokens.peek());
        Token openToken = tokens.expectNext(TokenType.OPEN);
        Expression expression = parse(tokens, declarationTable);
        tokens.expectNext(TokenType.CLOSE);
        return new ParsedExpression(expression, openToken, ExpressionType.VALUE);
    }

    private static ArrayList<ParsedExpression> parseExpressions(TokenReader tokens, DeclarationTable declarationTable) {
        ArrayList<ParsedExpression> expressions = new ArrayList<ParsedExpression>();
        ExpressionType lastExpression = ExpressionType.BINARY_OPERATION;
        while (!tokens.isAtEnd()) {
            if (nextIsUnaryOperator(tokens) && (lastExpression == ExpressionType.BINARY_OPERATION
                    || lastExpression == ExpressionType.UNARY_OPERATION)) {
                expressions.add(parseUnaryOperator(tokens));
                lastExpression = ExpressionType.UNARY_OPERATION;
            } else if (nextIsBinaryOperator(tokens) && (lastExpression == ExpressionType.VALUE)) {
                expressions.add(parseBinaryOperator(tokens));
                lastExpression = ExpressionType.BINARY_OPERATION;
            } else if (nextIsFunctionCall(tokens) && (lastExpression == ExpressionType.UNARY_OPERATION
                    || lastExpression == ExpressionType.BINARY_OPERATION)) {
                expressions.add(parseFunctionCall(tokens, declarationTable));
                lastExpression = ExpressionType.VALUE;
            } else if (nextIsVariable(tokens) && (lastExpression == ExpressionType.UNARY_OPERATION
                    || lastExpression == ExpressionType.BINARY_OPERATION)) {
                expressions.add(parseVariable(tokens, declarationTable));
                lastExpression = ExpressionType.VALUE;
            } else if (nextIsValue(tokens) && (lastExpression == ExpressionType.UNARY_OPERATION
                    || lastExpression == ExpressionType.BINARY_OPERATION)) {
                expressions.add(parseValue(tokens, declarationTable));
                lastExpression = ExpressionType.VALUE;
            } else if (nextIsOpen(tokens) && (lastExpression == ExpressionType.UNARY_OPERATION
                    || lastExpression == ExpressionType.BINARY_OPERATION)) {
                expressions.add(parseOpen(tokens, declarationTable));
                lastExpression = ExpressionType.VALUE;
            } else
                break;
        }

        return expressions;
    }

    private static Expression assembleExpression(List<ParsedExpression> expressions,
            DeclarationTable declarationTable) {
        if (expressions.size() == 0)
            return null;

        while (expressions.size() > 1) {
            int highestPrecedenceIndex = findHighestPrecedence(expressions);
            if (highestPrecedenceIndex == -1)
                throw new RuntimeException("Invalid expression: " + expressions);

            ParsedExpression expression = expressions.get(highestPrecedenceIndex);
            boolean isUnary = expression.getType() == ExpressionType.UNARY_OPERATION;
            OperatorType operatorType = getOperatorType(expression.getToken().getType(), isUnary);

            if (isUnary) {
                ParsedExpression nextExpression = expressions.get(highestPrecedenceIndex + 1);
                if (nextExpression.getExpression() == null)
                    throw new RuntimeException("Invalid expression: " + expressions);

                Expression operand = nextExpression.getExpression();
                declarationTable.validateOperatorUsage(operatorType, operand.getType(),
                        expression.getToken().getLine());

                UnaryOperatorDefinition unaryOperator = declarationTable.getUnaryOperator(operatorType,
                        operand.getType());
                UnaryOperator operatorExpression = new UnaryOperator(operatorType, operand,
                        unaryOperator.getReturnType());

                expressions.set(highestPrecedenceIndex,
                        new ParsedExpression(operatorExpression, expression.getToken(), ExpressionType.VALUE));
                expressions.remove(highestPrecedenceIndex + 1);
            } else {
                ParsedExpression leftExpression = expressions.get(highestPrecedenceIndex - 1);
                ParsedExpression rightExpression = expressions.get(highestPrecedenceIndex + 1);
                if (leftExpression.getExpression() == null || rightExpression.getExpression() == null)
                    throw new RuntimeException("Invalid expression: " + expressions);

                Expression left = leftExpression.getExpression();
                Expression right = rightExpression.getExpression();
                declarationTable.validateOperatorUsage(operatorType, left.getType(), right.getType(),
                        expression.getToken().getLine());

                BinaryOperatorDefinition binaryOperator = declarationTable.getBinaryOperator(operatorType,
                        left.getType(),
                        right.getType());
                OperatorExpression operatorExpression = new BinaryOperator(operatorType, left, right,
                        binaryOperator.getReturnType());
                expressions.set(highestPrecedenceIndex - 1,
                        new ParsedExpression(operatorExpression, expression.getToken(), ExpressionType.VALUE));
                expressions.remove(highestPrecedenceIndex);
                expressions.remove(highestPrecedenceIndex);
            }
        }
        return expressions.get(0).getExpression();
    }

    private static int findHighestPrecedence(List<ParsedExpression> expressions) {
        int highest = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < expressions.size(); i++) {
            ParsedExpression expression = expressions.get(i);
            if (expression.getType() == ExpressionType.UNARY_OPERATION) {
                TokenType operatorType = expression.getToken().getType();
                int precedence = getUnaryOperatorPrecedence(operatorType);
                Associativity associativity = getUnaryOperatorAssociativity(operatorType);
                if (precedence < highest
                        || (precedence == highest && associativity == Associativity.RIGHT_TO_LEFT && i > index)) {
                    highest = precedence;
                    index = i;
                }
            }

            if (expression.getType() == ExpressionType.BINARY_OPERATION) {
                TokenType operatorType = expression.getToken().getType();
                int precedence = getBinaryOperatorPrecedence(operatorType);
                Associativity associativity = getBinaryOperatorAssociativity(operatorType);
                if (precedence < highest
                        || (precedence == highest && associativity == Associativity.RIGHT_TO_LEFT && i > index)) {
                    highest = precedence;
                    index = i;
                }
            }

        }
        return index;
    }

}
