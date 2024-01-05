package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.WhileStatement;

public class WhileParser {
    public static Statement parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        tokens.expectNext(TokenType.WHILE);
        tokens.expectNext(TokenType.OPEN);

        Expression expression = ExpressionParser.parse(tokens, identifierTypes);
        if (!expression.getType().canBeConvertedTo(VariableType.BOOLEAN))
            throw new RuntimeException("Expected boolean expression at line " + tokens.peek().getLine());

        tokens.expectNext(TokenType.CLOSE);

        Statement statement = StatementParser.parse(tokens, identifierTypes.copy());
        return new WhileStatement(expression, statement);      
    }      
}
