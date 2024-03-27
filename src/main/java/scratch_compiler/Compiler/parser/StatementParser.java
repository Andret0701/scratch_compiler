package scratch_compiler.Compiler.parser;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class StatementParser {
    public static Statement parse(TokenReader tokens, DeclarationTable declarationTable) {
        if (tokens.isAtEnd())
            return null;

        TokenType type = tokens.peek().getType();
        if (type == TokenType.OPEN_BRACE)
            return parseScope(tokens, declarationTable);
        if (type == TokenType.IF)
            return IfParser.parse(tokens, declarationTable);
        if (type == TokenType.WHILE)
            return WhileParser.parse(tokens, declarationTable);
        if (type == TokenType.FOR)
            return ForParser.parse(tokens, declarationTable);

        Statement statement = null;
        switch (type) {
            case INT_DECLARATION:
                statement = VariableDeclarationParser.parse(tokens, declarationTable);
                break;
            case FLOAT_DECLARATION:
                statement = VariableDeclarationParser.parse(tokens, declarationTable);
                break;
            case BOOLEAN_DECLARATION:
                statement = VariableDeclarationParser.parse(tokens, declarationTable);
                break;
            case STRING_DECLARATION:
                statement = VariableDeclarationParser.parse(tokens, declarationTable);
                break;
            case RETURN:
                statement = FunctionDeclarationParser.parseReturnStatement(tokens, declarationTable);
                break;
            case IDENTIFIER:
                String name = tokens.peek().getValue();
                if (FunctionCallParser.nextIsFunctionCall(tokens, declarationTable))
                    statement = FunctionCallParser.parse(tokens, declarationTable);
                else if (SystemCallParser.nextIsSystemCall(tokens, declarationTable))
                    statement = SystemCallParser.parse(tokens, declarationTable);
                else if (declarationTable.isVariableDeclared(name))
                    statement = AssignmentParser.parse(tokens, declarationTable);
                else if (VariableDeclarationParser.nextIsVariableDeclaration(tokens, declarationTable))
                    statement = VariableDeclarationParser.parse(tokens, declarationTable);
                else
                    CompilerUtils.throwExpected("statement", tokens.peek().getLine(), tokens.peek());
                break;
            default:
                CompilerUtils.throwExpected("statement", tokens.peek().getLine(), tokens.peek());
        }

        tokens.expectNext(TokenType.SEMICOLON);
        return statement;
    }

    public static Scope parseScope(TokenReader tokens, DeclarationTable declarationTable) {
        if (!tokens.isNext(TokenType.OPEN_BRACE))
            return null;

        DeclarationTable innerDeclarationTable = declarationTable.copy();
        Scope scope = new Scope();

        tokens.next();
        while (!tokens.isAtEnd() && !tokens.isNext(TokenType.CLOSE_BRACE)) {
            Statement statement = StatementParser.parse(tokens, innerDeclarationTable);
            if (statement == null)
                break;
            scope.addStatement(statement);
        }

        tokens.expectNext(TokenType.CLOSE_BRACE);
        return scope;
    }
}
