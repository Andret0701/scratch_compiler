package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;

import java.util.ArrayList;

import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StringValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class VariableDeclarationParser {
    public static VariableDeclaration parse(TokenReader tokens, DeclarationTable declarationTable) {
        Type type = TypeParser.parse(tokens, declarationTable);
        if (type == new Type(VariableType.VOID))
            CompilerUtils.throwError("Cannot declare variable of type void", tokens.peek().getLine());

        Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifierToken.getValue();
        declarationTable.validateVariableDeclaration(name, identifierToken.getLine());
        tokens.expectNext(TokenType.ASSIGN);

        Expression value = ExpressionParser.parse(type, tokens, declarationTable);
        declarationTable.declareVariable(new Variable(name, type));
        return new VariableDeclaration(name, type, value);
    }

    // private static VariableDeclaration parseArrayDeclaration(TokenReader tokens,
    // DeclarationTable declarationTable,
    // TokenType declarationType, VariableType type) {

    // tokens.expectNext(TokenType.SQUARE_BRACKET_OPEN);
    // tokens.expectNext(TokenType.SQUARE_BRACKET_CLOSE);

    // Token identifier = tokens.expectNext(TokenType.IDENTIFIER);
    // String name = identifier.getValue();
    // declarationTable.validateVariableDeclaration(name, identifier.getLine());

    // tokens.expectNext(TokenType.ASSIGN);
    // tokens.expectNext(declarationType);
    // tokens.expectNext(TokenType.SQUARE_BRACKET_OPEN);

    // Expression expression = ExpressionParser.parse(tokens, declarationTable);
    // Expression.validateType(expression, VariableType.INT,
    // tokens.peek().getLine());

    // tokens.expectNext(TokenType.SQUARE_BRACKET_CLOSE);
    // tokens.expectNext(TokenType.SEMICOLON);

    // declarationTable.declareVariable(new Variable(name, type));
    // return new VariableDeclaration(name, type, expression);
    // }

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

    // int[] a = int[5];
    // int[5] a;
    // int[] a = {1, 2, 3};

}
