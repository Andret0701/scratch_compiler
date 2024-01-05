package scratch_compiler.Compiler.parser;

import java.util.ArrayList;
import java.util.List;

import javax.management.ValueExp;

import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenSubtype;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.AdditionExpression;
import scratch_compiler.Compiler.parser.expressions.BinaryOperationExpression;
import scratch_compiler.Compiler.parser.expressions.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.DivisionExpression;
import scratch_compiler.Compiler.parser.expressions.EqualsExpression;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.FloatValue;
import scratch_compiler.Compiler.parser.expressions.GreaterThanExpression;
import scratch_compiler.Compiler.parser.expressions.IntValue;
import scratch_compiler.Compiler.parser.expressions.LessThanExpression;
import scratch_compiler.Compiler.parser.expressions.ModulusExpression;
import scratch_compiler.Compiler.parser.expressions.MultiplicationExpression;
import scratch_compiler.Compiler.parser.expressions.OperationExpression;
import scratch_compiler.Compiler.parser.expressions.StringValue;
import scratch_compiler.Compiler.parser.expressions.SubtractionExpression;
import scratch_compiler.Compiler.parser.expressions.VariableValue;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.IdentifierTypes;

public class ExpressionParser {

    //fixme: add support for unary operators
    public static Expression parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        ArrayList<Expression> nodes = parseExpressionList(tokens, identifierTypes);
        return connectExpressionList(nodes);
    }



    private static int findLowestPrecedence(List<Expression> expressions) {
        int lowest = Integer.MAX_VALUE;
        int index = -1;

        for (int i= expressions.size()-1;i>=0;i--){
            Expression expression = expressions.get(i);
            int precedence = getPrecedence(expression);
            if (precedence == -1)
                continue;

            if (precedence <= lowest) {
                lowest = precedence;
                index = i;
            }
        }
        return index;
    }

    private static int getPrecedence(Expression node) {
        if (node instanceof OperationExpression)
            return ((OperationExpression) node).getPrecedence();
        return -1;
    }

    private static Expression connectExpressionList(List<Expression> expressions) {
        if(expressions.size()==0)
            return null;
        if (expressions.size() == 1)
            return expressions.get(0);

            int highestPrecedenceIndex = findLowestPrecedence(expressions);
            if (highestPrecedenceIndex==-1)
                throw new RuntimeException("Invalid expression: " + expressions);
    
            Expression expression = expressions.get(highestPrecedenceIndex);
            if (expression instanceof SubtractionExpression)
            {
                SubtractionExpression subtraction = (SubtractionExpression) expression;
                if (highestPrecedenceIndex==0)
                {
                    subtraction.setLeft(new IntValue(0));
                    Expression right = connectExpressionList(expressions.subList(1, expressions.size()));
                    if (canNegate(right))
                        return negate(right);
                    
                    subtraction.setRight(right);
                    return subtraction;
                }
            }
            if (expression instanceof BinaryOperationExpression)
            {
                BinaryOperationExpression binaryOperation = (BinaryOperationExpression) expression;
                binaryOperation.setLeft(connectExpressionList(expressions.subList(0, highestPrecedenceIndex)));
                binaryOperation.setRight(connectExpressionList(expressions.subList(highestPrecedenceIndex+1, expressions.size())));

                

                return binaryOperation;
            }           
            
        
        throw new RuntimeException("Invalid expression: " + expressions);
    }

    private static ArrayList<Expression> parseExpressionList(TokenReader tokens, IdentifierTypes identifierTypes) {
        ArrayList<Expression> expressions = new ArrayList<Expression>();
        while (!tokens.isAtEnd()) {
            Token token = tokens.peek();
            TokenType type = token.getType();
            TokenSubtype subtype = token.getSubtype();
            if (subtype == TokenSubtype.VALUE) {
                expressions.add(parseValue(tokens, identifierTypes));
            } else if (subtype== TokenSubtype.BINARY_OPERATOR) {
                TokenType operator = tokens.pop().getType();
                expressions.add(getBinaryOperation(operator));
            } else if (type == TokenType.OPEN) {
                tokens.expectNext(TokenType.OPEN);
                expressions.add(parse(tokens, identifierTypes));
                tokens.expectNext(TokenType.CLOSE);
            } else if (type==TokenType.IDENTIFIER)
            {
                String name = token.getValue();
                identifierTypes.validateUsage(name, tokens.peek().getLine());
                expressions.add(new VariableValue(name,identifierTypes.get(name)));
                tokens.next();
            }
            else 
                break;
        }

        return expressions;
    }

    public static Expression parseValue(TokenReader tokens, IdentifierTypes identifierTypes) {
        Token valueToken = tokens.pop();
        String value = valueToken.getValue();
        switch (valueToken.getType()) {
            case FLOAT:
                return new FloatValue(Double.parseDouble(value));
            case INT:
                return new IntValue(Integer.parseInt(value));
            case BOOLEAN:
                return new BooleanValue(Boolean.parseBoolean(value));
            case STRING:
                return new StringValue(value.substring(1, value.length() - 1)); 
            case IDENTIFIER:
                return new VariableValue(value, identifierTypes.get(value));
            case OPEN:
                tokens.expectNext(TokenType.OPEN);
                Expression expression = parse(tokens, identifierTypes);
                tokens.expectNext(TokenType.CLOSE);
                return expression;

            default:
                CompilerUtils.throwExpected("value", valueToken.getLine(),
                        valueToken.getValue());

        }

        return null;
    }

    public static BinaryOperationExpression getBinaryOperation(TokenType operator) {
        BinaryOperationExpression binaryOperation;
        switch (operator){
            case ADD:
                binaryOperation = new AdditionExpression();
                break;
            case SUB:
                binaryOperation = new SubtractionExpression();
                break;
            case MUL:
                binaryOperation = new MultiplicationExpression();
                break;
            case DIV:
                binaryOperation = new DivisionExpression();
                break;
            case MOD:
                binaryOperation = new ModulusExpression();
                break;
            case EQUALS:
                binaryOperation = new EqualsExpression();
                break;
            case GREATER_THAN:
                binaryOperation = new GreaterThanExpression();
                break;
            case LESS_THAN:
                binaryOperation = new LessThanExpression();
                break;
            default:
                throw new RuntimeException("Invalid operator " + operator);
        }
        return binaryOperation;
    }

    public static Expression getDefaultValue(VariableType type) {
        switch (type) {
            case FLOAT:
                return new FloatValue(0);
            case INT:
                return new IntValue(0);
            case BOOLEAN:
                return new BooleanValue(false);
            case STRING:
                return new StringValue("");
            default:
                throw new RuntimeException("Invalid type " + type);
        }
    }


    private static boolean canNegate(Expression expression)
    {
        return expression instanceof IntValue || expression instanceof FloatValue || expression instanceof BooleanValue;
    }

    private static Expression negate(Expression expression)
    {
        if (expression instanceof IntValue)
        {
            IntValue value = (IntValue) expression;
            return new IntValue(-value.getValue());
        }

        if (expression instanceof FloatValue)
        {
            FloatValue value = (FloatValue) expression;
            return new FloatValue(-value.getValue());
        }

        if (expression instanceof BooleanValue)
        {
            int value = ((BooleanValue) expression).getValue() ? 1 : 0;
            return new IntValue(-value);
        }


        throw new RuntimeException("Cannot negate: " + expression);
    }

}

