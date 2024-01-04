package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class StatementParser {
    public static Statement parse(TokenReader tokens, IdentifierTypes identifierTypes) {
        if (tokens.isAtEnd())
            return null;
        switch (tokens.peek().getType()) {
            case OPEN_BRACE:
                return parseScope(tokens, identifierTypes);
            case INT_DECLARATION:
                return DeclarationParser.parse(tokens, identifierTypes);
            case FLOAT_DECLARATION:
                return DeclarationParser.parse(tokens, identifierTypes);
            case BOOLEAN_DECLARATION:
                return DeclarationParser.parse(tokens, identifierTypes);
            case IDENTIFIER:
                return AssignmentParser.parse(tokens, identifierTypes);
            case IF:
                return IfParser.parse(tokens, identifierTypes);
            default:
                CompilerUtils.throwExpected("statement", tokens.peek().getLine(), tokens.peek());
        }
        throw new RuntimeException("Unreachable");
    }

    public static Statement parseScope(TokenReader tokens, IdentifierTypes identifierTypes) {
        if(!tokens.isNext(TokenType.OPEN_BRACE))
            return null;

        IdentifierTypes innerIdentifierTypes = identifierTypes.copy();
        Scope scope = new Scope(innerIdentifierTypes);

        tokens.next();
        while (!tokens.isAtEnd() && !tokens.isNext(TokenType.CLOSE_BRACE)) {
            Statement statement = StatementParser.parse(tokens, innerIdentifierTypes);
            if (statement == null)
                break;
            scope.addStatement(statement);
        }

        tokens.expect(TokenType.CLOSE_BRACE);

        tokens.next();
        return scope;
    }
}
