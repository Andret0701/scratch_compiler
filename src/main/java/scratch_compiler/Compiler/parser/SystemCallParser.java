package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.SystemCallStatement;

public class SystemCallParser {
    public static SystemCallStatement parse(TokenReader tokens, DeclarationTable declarationTable) {
        Token identifier = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifier.getValue();
        tokens.expectNext(TokenType.OPEN);

        declarationTable.validateSystemCallUsage(name, identifier.getLine());

        ArrayList<Expression> arguments = parseArguments(declarationTable.getSystemCall(name).getArguments(), tokens,
                declarationTable);
        tokens.expectNext(TokenType.CLOSE);

        return new SystemCallStatement(declarationTable.getSystemCall(name), arguments);
    }

    public static boolean nextIsSystemCall(TokenReader tokens, DeclarationTable declarationTable) {
        if (!tokens.isNext(TokenType.IDENTIFIER))
            return false;
        String systemCallName = tokens.peek().getValue();
        if (!declarationTable.isSystemCallDeclared(systemCallName))
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
