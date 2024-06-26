package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.WhileStatement;

public class WhileParser {
    public static Statement parse(TokenReader tokens, DeclarationTable identifierTypes, Type returnType) {
        tokens.expectNext(TokenType.WHILE);
        tokens.expectNext(TokenType.OPEN);

        Expression expression = ExpressionParser.parse(tokens, identifierTypes);
        if (!expression.getType().equals(new Type(VariableType.BOOL)))
            throw new RuntimeException("Expected boolean expression at line " + tokens.peek().getLine());

        tokens.expectNext(TokenType.CLOSE);

        Statement statement = StatementParser.parse(tokens, identifierTypes.copy(), returnType);
        return new WhileStatement(expression, statement);
    }
}
