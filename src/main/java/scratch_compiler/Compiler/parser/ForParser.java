package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.ForStatement;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ForParser {
    public static ForStatement parse(TokenReader tokens, DeclarationTable declarationTable) {
        tokens.expectNext(TokenType.FOR);
        tokens.expectNext(TokenType.OPEN);

        DeclarationTable innerDeclarationTable = declarationTable.copy();
        VariableDeclaration declaration = VariableDeclarationParser.parse(tokens, innerDeclarationTable);

        tokens.expectNext(TokenType.SEMICOLON);

        Expression loopCondition = ExpressionParser.parse(tokens, innerDeclarationTable);
        if (!loopCondition.getType().equals(new Type(VariableType.BOOLEAN)))
            throw new RuntimeException("Expected boolean expression at line " + tokens.peek().getLine());

        tokens.expectNext(TokenType.SEMICOLON);

        Assignment increment = AssignmentParser.parse(tokens, innerDeclarationTable);

        tokens.expectNext(TokenType.CLOSE);

        Statement statement = StatementParser.parse(tokens, innerDeclarationTable);
        return new ForStatement(declaration, loopCondition, increment, statement);
    }

}
