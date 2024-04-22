package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Statement;

public class IfParser {
    public static Statement parse(TokenReader tokens, DeclarationTable identifierTypes, Type returnType) {
        tokens.expectNext(TokenType.IF);
        tokens.expectNext(TokenType.OPEN);

        Expression expression = ExpressionParser.parse(tokens, identifierTypes);
        if (expression == null)
            CompilerUtils.throwError("Expected expression", tokens.peek().getLine());

        if (!expression.getType().equals(new Type(VariableType.BOOL)))
            throw new RuntimeException("Expected boolean expression at line " + tokens.peek().getLine());

        tokens.expectNext(TokenType.CLOSE);

        Statement statement = StatementParser.parse(tokens, identifierTypes.copy(), returnType);
        if (statement == null)
            throw new RuntimeException("Expected statement at line " + tokens.peek().getLine());

        if (tokens.isNext(TokenType.ELSE)) {
            tokens.next();
            Statement elseStatement = StatementParser.parse(tokens, identifierTypes.copy(), returnType);
            if (elseStatement == null)
                throw new RuntimeException("Expected statement at line " + tokens.peek().getLine());
            return new IfStatement(expression, statement, elseStatement);
        }

        return new IfStatement(expression, statement);
    }
}
