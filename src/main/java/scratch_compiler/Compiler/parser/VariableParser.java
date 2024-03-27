package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;

public class VariableParser {
    public static VariableValue parse(TokenReader tokens, DeclarationTable declarationTable) {

        Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
        Expression index = null;
        if (isIndexing(tokens))
            index = parseIndexing(tokens, declarationTable);

        VariableReference reference = parseVariableReference(tokens, declarationTable);

        String name = identifierToken.getValue();
        declarationTable.validateVariableUsage(name, identifierToken.getLine());
        Type type = declarationTable.getVariable(name).getType();

        if (!type.getType().containsReference(reference))
            CompilerUtils.throwError("Variable " + name + " does not contain reference " + reference,
                    identifierToken.getLine());

        if (index != null)
            return new VariableValue(name, reference, type, index);
        return new VariableValue(name, reference, type);
    }

    private static boolean isIndexing(TokenReader tokens) {
        return tokens.isNext(TokenType.SQUARE_BRACKET_OPEN);
    }

    private static Expression parseIndexing(TokenReader tokens, DeclarationTable declarationTable) {
        Token squareToken = tokens.expectNext(TokenType.SQUARE_BRACKET_OPEN);
        Expression index = ExpressionParser.parse(tokens, declarationTable);
        if (!index.getType().equals(new Type(TypeDefinition.INT)))
            CompilerUtils.throwExpected("int", squareToken.getLine(), index.getType().toString());
        tokens.expectNext(TokenType.SQUARE_BRACKET_CLOSE);
        return index;
    }

    private static VariableReference parseVariableReference(TokenReader tokens, DeclarationTable declarationTable) {
        if (!tokens.isNext(TokenType.DOT))
            return null;

        tokens.expectNext(TokenType.DOT);
        Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifierToken.getValue();

        VariableReference next = parseVariableReference(tokens, declarationTable);
        return new VariableReference(name, next);
    }

}
