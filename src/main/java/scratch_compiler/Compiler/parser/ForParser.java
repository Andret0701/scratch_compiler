package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.ForStatement;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ForParser {
        public static ForStatement parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        tokens.expectNext(TokenType.FOR);
        tokens.expectNext(TokenType.OPEN);

        IdentifierTypes innerIdentifierTypes = identifierTypes.copy();
        VariableDeclaration declaration = DeclarationParser.parse(tokens, innerIdentifierTypes);

        tokens.expectNext(TokenType.SEMICOLON);

        Expression loopCondition = ExpressionParser.parse(tokens, innerIdentifierTypes);
        if (!loopCondition.getType().canBeConvertedTo(VariableType.BOOLEAN))
            throw new RuntimeException("Expected boolean expression at line " + tokens.peek().getLine());

        tokens.expectNext(TokenType.SEMICOLON);

        Assignment increment = AssignmentParser.parse(tokens, innerIdentifierTypes);


        tokens.expectNext(TokenType.CLOSE);

        Statement statement = StatementParser.parse(tokens, innerIdentifierTypes);
        return new ForStatement(declaration, loopCondition, increment, statement);
    }      
    
}
