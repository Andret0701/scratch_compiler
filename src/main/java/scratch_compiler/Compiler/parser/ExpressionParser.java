package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.BinaryOperationNode;
import scratch_compiler.Compiler.parser.nodes.BooleanNode;
import scratch_compiler.Compiler.parser.nodes.Expression;
import scratch_compiler.Compiler.parser.nodes.FloatNode;
import scratch_compiler.Compiler.parser.nodes.IntNode;
import scratch_compiler.Compiler.parser.nodes.OperationNode;
import scratch_compiler.Compiler.parser.nodes.ValueNode;
import scratch_compiler.Compiler.parser.nodes.VariableNode;
import scratch_compiler.Variable;
import scratch_compiler.Compiler.CompilerUtils;

public class ExpressionParser {
    // alt er node
    // value node identifier + number
    // operation node + - * /

    public static Expression parse(TokenReader tokens) {
        ArrayList<Expression> nodes = parseExpressionList(tokens);

        if (nodes.size() == 1)
            return nodes.get(0);

        while (nodes.size() > 1) {
            int highestPrecedenceIndex = findHighestPrecedence(nodes);
            BinaryOperationNode node = (BinaryOperationNode) nodes.get(highestPrecedenceIndex);
            node.setLeft(nodes.get(highestPrecedenceIndex - 1));
            node.setRight(nodes.get(highestPrecedenceIndex + 1));
            nodes.remove(highestPrecedenceIndex + 1);
            nodes.remove(highestPrecedenceIndex - 1);
            nodes.set(highestPrecedenceIndex - 1, node);
        }

        return nodes.get(0);
    }

    private static int findHighestPrecedence(ArrayList<Expression> nodes) {
        int highestPrecedence = 0;
        int highestPrecedenceIndex = -1;

        for (int i = 1; i < nodes.size() - 1; i += 2) {
            BinaryOperationNode node = (BinaryOperationNode) nodes.get(i);
            int precedence = node.getPrecedence();
            if (precedence > highestPrecedence) {
                highestPrecedence = precedence;
                highestPrecedenceIndex = i;
            }
        }
        return highestPrecedenceIndex;
    }

    private static ArrayList<Expression> parseExpressionList(TokenReader tokens) {
        ArrayList<Expression> nodes = new ArrayList<Expression>();

        BinaryOperationNode minusOperator = null;
        if (tokens.peek().isOperator() && tokens.peek().getType() == TokenType.MINUS) {
            Token minusToken = tokens.pop();
            minusOperator = new BinaryOperationNode(minusToken);
        }

        Expression node = parseValue(tokens);
        if (node == null)
            CompilerUtils.throwExpected("value", tokens.peek().getLine(), tokens.peek());

        if (minusOperator != null) {
            minusOperator.setRight(node);
            minusOperator
                    .setLeft(new FloatNode(
                            new Token(node.getToken().getType(), "0", minusOperator.getToken().getLine())));

            node = minusOperator;
        }

        nodes.add(node);

        while (tokens.peek() != null && tokens.peek().isOperator()) {
            // get operator
            Token operatorToken = tokens.pop();
            OperationNode operator = new BinaryOperationNode(operatorToken);
            nodes.add(operator);

            // get value
            node = parseValue(tokens);
            if (node == null)
                CompilerUtils.throwExpected("value", tokens.peek().getLine(), tokens.peek());
            nodes.add(node);
        }

        return nodes;
    }

    public static Expression parseValue(TokenReader tokens) {
        Token valueToken = tokens.peek();

        switch (valueToken.getType()) {
            case FLOAT:
                return new FloatNode(tokens.pop());
            case INT:
                return new IntNode(tokens.pop());
            case BOOLEAN:
                return new BooleanNode(tokens.pop());
            case IDENTIFIER:
                return new VariableNode(tokens.pop());
            case OPEN:
                // skip open token
                tokens.next();
                Expression node = parse(tokens);

                // assert next token is close
                Token closeToken = tokens.peek();
                CompilerUtils.assertIsType(closeToken, TokenType.CLOSE);

                // skip close token
                tokens.next();
                return node;

            default:
                CompilerUtils.throwExpected("value", valueToken.getLine(),
                        valueToken.getValue());

        }

        return null;
    }
}
