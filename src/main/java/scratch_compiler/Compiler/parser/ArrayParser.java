package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.ArrayDeclarationValue;
import scratch_compiler.Compiler.parser.expressions.values.ArrayValue;

public class ArrayParser {
    public static ArrayDeclarationValue parseArrayDeclaration(
            TypeDefinition type, TokenReader tokens,
            DeclarationTable declarationTable) {
        TypeDefinition arrayType = TypeParser.parseDefinition(tokens, declarationTable);
        if (!arrayType.equals(type))
            CompilerUtils.throwError("Array type mismatch", tokens.peek().getLine());

        tokens.expectNext(TokenType.SQUARE_BRACKET_OPEN);
        Expression size = ExpressionParser.parse(new Type(VariableType.INT), tokens, declarationTable);
        tokens.expectNext(TokenType.SQUARE_BRACKET_CLOSE);

        return new ArrayDeclarationValue(type, size);
    }

    public static ArrayValue parseArrayValue(TypeDefinition type, TokenReader tokens,
            DeclarationTable declarationTable) {
        tokens.expectNext(TokenType.OPEN_BRACE);
        ArrayList<Expression> values = new ArrayList<>();
        while (true) {
            Expression value = ExpressionParser.parse(type, tokens, declarationTable);
            if (value == null)
                CompilerUtils.throwError("Invalid array value", tokens.peek().getLine());
            values.add(value);
            if (!tokens.isNext(TokenType.COMMA)) {
                tokens.expectNext(TokenType.CLOSE_BRACE);
                return new ArrayValue(type, values);
            }
            tokens.expectNext(TokenType.COMMA);
        }
    }
}
