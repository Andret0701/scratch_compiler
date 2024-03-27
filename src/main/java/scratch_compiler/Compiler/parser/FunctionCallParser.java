package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.FunctionCall;

public class FunctionCallParser {
    public static FunctionCall parse(TokenReader tokens, DeclarationTable declarationTable) {
        Token identifier = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifier.getValue();
        tokens.expectNext(TokenType.OPEN);

        declarationTable.validateFunctionUsage(name, identifier.getLine());

        ArrayList<Expression> arguments = parseArguments(declarationTable.getFunction(name).getArguments(), tokens,
                declarationTable);
        tokens.expectNext(TokenType.CLOSE);

        return new FunctionCall(declarationTable.getFunction(name), arguments);
    }

    public static boolean nextIsFunctionCall(TokenReader tokens, DeclarationTable declarationTable) {
        if (!tokens.isNext(TokenType.IDENTIFIER))
            return false;
        String functionName = tokens.peek().getValue();
        if (!declarationTable.isFunctionDeclared(functionName))
            return false;

        return true;
    }

    private static ArrayList<Expression> parseArguments(ArrayList<Variable> arguments, TokenReader tokens,
            DeclarationTable declarationTable) {
        ArrayList<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < arguments.size(); i++) {
            Variable argument = arguments.get(i);
            Expression expression = ExpressionParser.parse(argument.getType(), tokens, declarationTable);
            if (expression == null)
                throw new RuntimeException("Invalid argument at line " + tokens.peek().getLine());
            expressions.add(expression);

            if (i < arguments.size() - 1)
                tokens.expectNext(TokenType.COMMA);
        }
        return expressions;
    }
}
