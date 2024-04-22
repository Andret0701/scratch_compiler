package scratch_compiler.Compiler.parser;

import java.util.ArrayList;
import java.util.List;

import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.FunctionCallExpression;
import scratch_compiler.Compiler.parser.expressions.IndexExpression;
import scratch_compiler.Compiler.parser.expressions.ParsedExpression;
import scratch_compiler.Compiler.parser.expressions.ReferenceExpression;
import scratch_compiler.Compiler.parser.expressions.SizeOfExpression;
import scratch_compiler.Compiler.parser.expressions.SystemCallExpression;
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
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.SystemCall;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.UnaryOperatorDefinition;
import scratch_compiler.Compiler.Variable;

public class ExpressionParser {
    public static Expression parse(TypeDefinition type, TokenReader tokens, DeclarationTable declarationTable) {
        return parse(new Type(type), tokens, declarationTable);
    }

    public static Expression parse(Type type, TokenReader tokens, DeclarationTable declarationTable) {
        if (tokens.isNext(TokenType.OPEN_BRACE)) {
            if (type.isArray())
                return ArrayParser.parseArrayValue(type.getType(), tokens, declarationTable);
            else
                return StructParser.parse(type.getType(), tokens, declarationTable);
        }

        Expression expression = parse(tokens, declarationTable);
        if (expression == null)
            return null;

        declarationTable.validateTypeConversion(expression.getType(), type, tokens.peek().getLine());
        return declarationTable.convertExpression(expression, type);
    }

    public static Expression parse(TokenReader tokens, DeclarationTable declarationTable) {
        if (TypeParser.nextIsType(tokens, declarationTable))
            return ArrayParser.parseArrayDeclaration(tokens, declarationTable);

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

    private static boolean nextIsFunctionCall(TokenReader tokens, DeclarationTable declarationTable) {
        return tokens.isAt(0, TokenType.IDENTIFIER) && tokens.isAt(1, TokenType.OPEN)
                && declarationTable.isFunctionDeclared(tokens.peek().getValue());
    }

    private static ParsedExpression parseFunctionCall(TokenReader tokens, DeclarationTable declarationTable) {
        if (!nextIsFunctionCall(tokens, declarationTable))
            CompilerUtils.throwExpected("function call", tokens.peek().getLine(), tokens.peek());

        Token functionToken = tokens.expectNext(TokenType.IDENTIFIER);

        String functionName = functionToken.getValue();
        declarationTable.validateFunctionUsage(functionName, tokens.peek().getLine());
        Function function = declarationTable.getFunction(functionName);

        tokens.expectNext(TokenType.OPEN);
        ArrayList<Expression> arguments = new ArrayList<Expression>();
        for (int i = 0; i < function.getArguments().size(); i++) {
            Variable argument = function.getArguments().get(i);
            if (tokens.peek().getType() == TokenType.CLOSE)
                CompilerUtils.throwError("Expected " + argument.getName() + " argument", tokens.peek().getLine());

            Expression argumentExpression = parse(argument.getType(), tokens, declarationTable);
            if (argumentExpression == null)
                CompilerUtils.throwError("Expected " + argument.getName() + " argument", tokens.peek().getLine());

            arguments.add(argumentExpression);
            if (i < function.getArguments().size() - 1) {
                if (tokens.peek().getType() != TokenType.COMMA)
                    CompilerUtils.throwError("Too few arguments for function " + functionName, tokens.peek().getLine());
                tokens.pop();
            }
        }

        tokens.expectNext(TokenType.CLOSE);

        FunctionCallExpression functionCall = new FunctionCallExpression(function, arguments);

        return new ParsedExpression(functionCall, functionToken, ExpressionType.VALUE);
    }

    public static boolean nextIsSystemCall(TokenReader tokens, DeclarationTable declarationTable) {
        return tokens.isAt(0, TokenType.IDENTIFIER) && tokens.isAt(1, TokenType.OPEN)
                && declarationTable.isSystemCallDeclared(tokens.peek().getValue());
    }

    public static ParsedExpression parseSystemCall(TokenReader tokens, DeclarationTable declarationTable) {
        if (!nextIsSystemCall(tokens, declarationTable))
            CompilerUtils.throwExpected("system call", tokens.peek().getLine(), tokens.peek());

        Token systemCallToken = tokens.expectNext(TokenType.IDENTIFIER);

        String systemCallName = systemCallToken.getValue();
        declarationTable.validateSystemCallUsage(systemCallName, tokens.peek().getLine());
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

        SystemCall function = declarationTable.getSystemCall(systemCallName);
        SystemCallExpression systemCall = new SystemCallExpression(function, arguments);

        return new ParsedExpression(systemCall, systemCallToken, ExpressionType.VALUE);
    }

    public static boolean nextIsVariable(TokenReader tokens, DeclarationTable declarationTable) {
        TokenType type = tokens.peek().getType();
        return type == TokenType.IDENTIFIER && declarationTable
                .isVariableDeclared(tokens.peek().getValue());
    }

    public static ParsedExpression parseVariable(TokenReader tokens, DeclarationTable declarationTable) {
        if (!nextIsVariable(tokens, declarationTable))
            CompilerUtils.throwExpected("variable", tokens.peek().getLine(), tokens.peek());

        Token variableToken = tokens.peek();
        String variableName = tokens.expectNext(TokenType.IDENTIFIER).getValue();
        declarationTable.validateVariableUsage(variableName, variableToken.getLine());
        Variable variable = declarationTable.getVariable(variableName);

        return new ParsedExpression(new VariableValue(variable.getName(), variable.getType()), variableToken,
                ExpressionType.VALUE);
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

    private static boolean nextIsSize(TokenReader tokenReader) {
        return tokenReader.isNext(TokenType.SIZE);
    }

    private static ParsedExpression parseSize(TokenReader tokenReader, DeclarationTable declarationTable) {
        if (!nextIsSize(tokenReader))
            CompilerUtils.throwExpected("size of", tokenReader.peek().getLine(), tokenReader.peek());

        tokenReader.expectNext(TokenType.SIZE);
        return new ParsedExpression(null, tokenReader.peek(), ExpressionType.SIZE);
    }

    public static boolean nextIsIndex(TokenReader tokenReader) {
        return tokenReader.isNext(TokenType.SQUARE_BRACKET_OPEN);
    }

    public static ParsedExpression parseIndex(TokenReader tokenReader, DeclarationTable declarationTable) {
        if (!nextIsIndex(tokenReader))
            CompilerUtils.throwExpected("index", tokenReader.peek().getLine(), tokenReader.peek());

        tokenReader.expectNext(TokenType.SQUARE_BRACKET_OPEN);
        Expression index = parse(tokenReader, declarationTable);
        tokenReader.expectNext(TokenType.SQUARE_BRACKET_CLOSE);
        return new ParsedExpression(null, tokenReader.peek(), ExpressionType.INDEX, index);
    }

    public static boolean nextIsReference(TokenReader tokenReader) {
        return tokenReader.isNext(TokenType.DOT);
    }

    public static ParsedExpression parseReference(TokenReader tokenReader, DeclarationTable declarationTable) {
        if (!nextIsReference(tokenReader))
            CompilerUtils.throwExpected("reference", tokenReader.peek().getLine(), tokenReader.peek());

        tokenReader.expectNext(TokenType.DOT);
        Token identifierToken = tokenReader.expectNext(TokenType.IDENTIFIER);
        String identifier = identifierToken.getValue();
        return new ParsedExpression(null, identifierToken, ExpressionType.REFERENCE, identifier);
    }

    private static ArrayList<ParsedExpression> parseExpressions(TokenReader tokens, DeclarationTable declarationTable) {
        ArrayList<ParsedExpression> expressions = new ArrayList<ParsedExpression>();
        while (!tokens.isAtEnd()) {
            boolean hasLeftValue = expressions.size() > 0
                    && ((expressions.get(expressions.size() - 1).getExpression() != null) || (expressions
                            .get(expressions.size() - 1).getType() == ExpressionType.INDEX)
                            || (expressions.get(expressions.size() - 1).getType() == ExpressionType.REFERENCE));
            if (nextIsUnaryOperator(tokens) && !hasLeftValue) {
                expressions.add(parseUnaryOperator(tokens));
            } else if (nextIsBinaryOperator(tokens) && hasLeftValue) {
                expressions.add(parseBinaryOperator(tokens));
            } else if (nextIsFunctionCall(tokens, declarationTable)) {
                expressions.add(parseFunctionCall(tokens, declarationTable));
            } else if (nextIsSystemCall(tokens, declarationTable)) {
                expressions.add(parseSystemCall(tokens, declarationTable));
            } else if (nextIsVariable(tokens, declarationTable)) {
                expressions.add(parseVariable(tokens, declarationTable));
            } else if (nextIsValue(tokens)) {
                expressions.add(parseValue(tokens, declarationTable));
            } else if (nextIsOpen(tokens)) {
                expressions.add(parseOpen(tokens, declarationTable));
            } else if (nextIsSize(tokens)) {
                expressions.add(parseSize(tokens, declarationTable));
            } else if (nextIsIndex(tokens)) {
                expressions.add(parseIndex(tokens, declarationTable));
            } else if (nextIsReference(tokens)) {
                expressions.add(parseReference(tokens, declarationTable));
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

            if (expression.getType() == ExpressionType.UNARY_OPERATION
                    || expression.getType() == ExpressionType.BINARY_OPERATION) {
                boolean isUnary = expression.getType() == ExpressionType.UNARY_OPERATION;
                OperatorType operatorType = getOperatorType(expression.getToken().getType(), isUnary);
                if (expression.getType() == ExpressionType.UNARY_OPERATION) {
                    ParsedExpression nextExpression = expressions.get(highestPrecedenceIndex + 1);
                    if (nextExpression.getExpression() == null)
                        throw new RuntimeException("Invalid expression: " + expressions);

                    Expression operand = nextExpression.getExpression();
                    declarationTable.validateOperatorUsage(operatorType, operand.getType(),
                            expression.getToken().getLine());

                    UnaryOperatorDefinition unaryOperator = declarationTable.getUnaryOperator(operatorType,
                            operand.getType().getType());
                    UnaryOperator operatorExpression = new UnaryOperator(operatorType, operand,
                            new Type(unaryOperator.getReturnType()));

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
                            left.getType().getType(),
                            right.getType().getType());
                    BinaryOperator operatorExpression = new BinaryOperator(operatorType, left, right,
                            new Type(binaryOperator.getReturnType()));
                    expressions.set(highestPrecedenceIndex - 1,
                            new ParsedExpression(operatorExpression, expression.getToken(), ExpressionType.VALUE));
                    expressions.remove(highestPrecedenceIndex);
                    expressions.remove(highestPrecedenceIndex);
                }
            } else if (expression.getType() == ExpressionType.REFERENCE) {
                int leftIndex = highestPrecedenceIndex - 1;
                if (leftIndex < 0)
                    CompilerUtils.throwError("Expected something before reference", expression.getToken().getLine());

                ParsedExpression leftExpression = expressions.get(leftIndex);
                if (leftExpression.getExpression() == null)
                    CompilerUtils.throwError("Expected something referencable before reference",
                            expression.getToken().getLine());

                Expression left = leftExpression.getExpression();
                if (left.getType().isArray())
                    CompilerUtils.throwError("Cannot reference array", expression.getToken().getLine());

                if (left.getType().getType().getType() != VariableType.STRUCT)
                    CompilerUtils.throwError("Cannot reference non-struct", expression.getToken().getLine());

                String reference = (String) expression.getData();

                Expression referenceExpression = new ReferenceExpression(reference, left);
                // set the new expression

                expressions.set(leftIndex,
                        new ParsedExpression(referenceExpression, expression.getToken(), ExpressionType.VALUE));
                expressions.remove(highestPrecedenceIndex);
            } else if (expression.getType() == ExpressionType.INDEX) {
                int leftIndex = highestPrecedenceIndex - 1;
                if (leftIndex < 0)
                    CompilerUtils.throwError("Expected something before index", expression.getToken().getLine());

                ParsedExpression leftExpression = expressions.get(leftIndex);
                if (leftExpression.getExpression() == null)
                    CompilerUtils.throwError("Expected something indexable before index",
                            expression.getToken().getLine());

                Expression left = leftExpression.getExpression();
                if (!left.getType().isArray())
                    CompilerUtils.throwError("Cannot index non-array", expression.getToken().getLine());

                Expression index = (Expression) expression.getData();
                Expression indexExpression = new IndexExpression(left, index);
                // set the new expression
                expressions.set(leftIndex,
                        new ParsedExpression(indexExpression, expression.getToken(), ExpressionType.VALUE));
                expressions.remove(highestPrecedenceIndex);

            } else if (expression.getType() == ExpressionType.SIZE) {
                int rightIndex = highestPrecedenceIndex + 1;
                if (rightIndex >= expressions.size())
                    CompilerUtils.throwError("Expected something after size", expression.getToken().getLine());

                ParsedExpression rightExpression = expressions.get(rightIndex);
                if (rightExpression.getExpression() == null)
                    CompilerUtils.throwError("Expected something sizeable after size", expression.getToken().getLine());

                Expression right = rightExpression.getExpression();
                if (!right.getType().isArray())
                    CompilerUtils.throwError("Cannot get size of non-array", expression.getToken().getLine());

                Expression sizeExpression = new SizeOfExpression(right);
                // set the new expression
                expressions.set(highestPrecedenceIndex,
                        new ParsedExpression(sizeExpression, expression.getToken(), ExpressionType.VALUE));
                expressions.remove(rightIndex);
            }

            else
                CompilerUtils.throwError("Invalid expression: " + expressions, expression.getToken().getLine());

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

            if (expression.getType() == ExpressionType.INDEX || expression.getType() == ExpressionType.REFERENCE) {
                int precedence = -100;
                if (precedence < highest) {
                    highest = precedence;
                    index = i;
                }
            }

            if (expression.getType() == ExpressionType.SIZE) {
                int precedence = -99;
                if (precedence < highest) {
                    highest = precedence;
                    index = i;
                }
            }

        }
        return index;
    }

}
