package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.Expression;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Statement;

public class IfParser {
    public static Statement parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        if (!tokens.isNext(TokenType.IF))
            return null;
        tokens.next();

        tokens.expect(TokenType.OPEN);
        tokens.next();
        Expression expression = ExpressionParser.parse(tokens);
        if (expression == null)
            throw new RuntimeException("Expected expression at line " + tokens.peek().getLine());
        if (expression.getType(identifierTypes) != TokenType.BOOLEAN)
            throw new RuntimeException("Expected boolean expression at line " + tokens.peek().getLine());

        tokens.expect(TokenType.CLOSE);
        tokens.next();

        Statement statement = StatementParser.parse(tokens, identifierTypes);
        if (statement == null)
            throw new RuntimeException("Expected statement at line " + tokens.peek().getLine());
        
        if (tokens.isNext(TokenType.ELSE)) {
            tokens.next();
            Statement elseStatement = StatementParser.parse(tokens, identifierTypes);
            if (elseStatement == null)
                throw new RuntimeException("Expected statement at line " + tokens.peek().getLine());
            return new IfStatement(expression, statement, elseStatement);
        }

        return new IfStatement(expression, statement);      
    }   
}
