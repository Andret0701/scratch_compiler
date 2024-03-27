package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class VariableDeclarationParser {
    public static VariableDeclaration parse(TokenReader tokens, DeclarationTable declarationTable) {
        Type type = TypeParser.parse(tokens, declarationTable);
        if (type.getType() == new TypeDefinition(VariableType.VOID))
            CompilerUtils.throwError("Cannot declare variable of type void", tokens.peek().getLine());

        Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifierToken.getValue();
        declarationTable.validateVariableDeclaration(name, identifierToken.getLine());

        Expression value = null;
        if (tokens.isNext(TokenType.ASSIGN)) {
            tokens.expectNext(TokenType.ASSIGN);
            value = ExpressionParser.parse(type, tokens, declarationTable);
        }

        declarationTable.declareVariable(name, type);
        return new VariableDeclaration(name, type, value);
    }

    public static boolean nextIsVariableDeclaration(TokenReader tokens, DeclarationTable declarationTable) {
        if (!TypeParser.nextIsType(tokens, declarationTable))
            return false;

        TokenType nextType = tokens.peek(1).getType();
        return nextType == TokenType.IDENTIFIER || nextType == TokenType.SQUARE_BRACKET_OPEN;
    }

}
