package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;

import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.ControlFlowStatement;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.ReturnStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class FunctionDeclarationParser {
    public static FunctionDeclaration parse(TokenReader tokens, DeclarationTable declarationTable) {
        Type returnType = TypeParser.parse(tokens, declarationTable);

        Token identifierToken = tokens.expectNext(TokenType.IDENTIFIER);
        String name = identifierToken.getValue();
        declarationTable.validateFunctionDeclaration(name, identifierToken.getLine());
        tokens.expectNext(TokenType.OPEN);
        ArrayList<Variable> arguments = ArgumentParser.parseArguments(tokens, declarationTable);
        tokens.expectNext(TokenType.CLOSE);

        declarationTable.declareFunction(new Function(name, returnType, arguments));
        DeclarationTable functionDeclarationTable = declarationTable.copy();
        for (Variable argument : arguments)
            functionDeclarationTable.declareVariable(argument);

        Scope scope = StatementParser.parseScope(tokens, functionDeclarationTable);
        fixReturnType(scope, returnType, functionDeclarationTable);
        if (!scopeReturns(scope) && !returnType.equals(new Type(VariableType.VOID)))
            throw new RuntimeException("Function " + name + " does not return a value in all code paths");

        return new FunctionDeclaration(new Function(name, returnType, arguments), scope);
    }

    public static boolean nextIsFunctionDeclaration(TokenReader tokens, DeclarationTable declarationTable) {
        return TypeParser.nextIsType(tokens, declarationTable) && tokens.peek(1).getType() == TokenType.IDENTIFIER
                && tokens.peek(2).getType() == TokenType.OPEN;
    }

    public static ReturnStatement parseReturnStatement(TokenReader tokens,
            DeclarationTable declarationTable) {
        tokens.expectNext(TokenType.RETURN);
        Expression expression = ExpressionParser.parse(tokens, declarationTable);
        return new ReturnStatement(expression);
    }

    private static void fixReturnType(Statement statement, Type returnType, DeclarationTable declarationTable) {
        if (statement instanceof ReturnStatement) {
            ReturnStatement returnStatement = (ReturnStatement) statement;
            declarationTable.validateTypeConversion(returnStatement.getExpression(), returnType, 0);
            returnStatement
                    .setExpression(declarationTable.convertExpression(returnStatement.getExpression(), returnType));
            return;
        }

        for (Statement child : statement.getChildren())
            fixReturnType(child, returnType, declarationTable);
    }

    private static boolean statementReturns(Statement statement) {
        if (statement instanceof ReturnStatement)
            return true;
        if (statement instanceof IfStatement)
            return ifStatementReturns((IfStatement) statement);
        if (statement instanceof Scope)
            return scopeReturns((Scope) statement);
        return false;
    }

    private static boolean scopeReturns(Scope scope) {
        boolean hasReturn = false;
        for (Statement statement : scope.getStatements()) {
            if (hasReturn)
                throw new RuntimeException("Unreachable code after return statement");

            if (statementReturns(statement))
                hasReturn = true;
        }
        return hasReturn;
    }

    private static boolean ifStatementReturns(IfStatement ifStatement) {
        if (ifStatement.getElseStatement() == null)
            return false;

        return statementReturns(ifStatement.getStatement()) && statementReturns(ifStatement.getElseStatement());
    }

}
