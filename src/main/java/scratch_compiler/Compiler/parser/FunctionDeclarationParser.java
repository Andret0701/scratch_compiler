package scratch_compiler.Compiler.parser;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompilerUtils;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.ArrayDeclarationValue;
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
            functionDeclarationTable.declareVariable(argument.getName(), argument.getType());

        Scope scope = StatementParser.parseScope(tokens, functionDeclarationTable, returnType);
        fixReturnType(scope, returnType, functionDeclarationTable);
        if (!scopeReturns(scope) && !returnType.equals(new Type(VariableType.VOID)))
            throw new RuntimeException("Function " + name + " does not return a value in all code paths");

        return new FunctionDeclaration(new Function(name, returnType, arguments), scope);
    }

    public static boolean nextIsFunctionDeclaration(TokenReader tokens, DeclarationTable declarationTable) {
        System.out.println(tokens.peek() + " " + tokens.peek(1));
        return TypeParser.nextIsType(tokens, declarationTable) && ((tokens.peek(1).getType() == TokenType.IDENTIFIER
                && tokens.peek(2).getType() == TokenType.OPEN)
                || (tokens.peek(1).getType() == TokenType.SQUARE_BRACKET_OPEN
                        && tokens.peek(3).getType() == TokenType.IDENTIFIER
                        && tokens.peek(4).getType() == TokenType.OPEN));
    }

    public static ReturnStatement parseReturnStatement(TokenReader tokens,
            DeclarationTable declarationTable, Type returnType) {
        Token returnToken = tokens.expectNext(TokenType.RETURN);
        Expression expression = ExpressionParser.parse(returnType, tokens, declarationTable);
        if (expression == null)
            CompilerUtils.throwError("Return statement must have an expression", returnToken.getLine());

        if (expression instanceof ArrayDeclarationValue)
            CompilerUtils.throwError("Cannot return uninitialized array", returnToken.getLine());

        return new ReturnStatement(expression);
    }

    private static void fixReturnType(Statement statement, Type returnType,
            DeclarationTable declarationTable) {
        if (statement instanceof ReturnStatement) {
            ReturnStatement returnStatement = (ReturnStatement) statement;
            declarationTable.validateTypeConversion(returnStatement.getExpression(), returnType, 0);
            returnStatement
                    .setExpression(declarationTable.convertExpression(returnStatement.getExpression(), returnType));
            return;
        }

        for (int i = 0; i < statement.getScopeCount(); i++) {
            for (Statement child : statement.getScope(i).getStatements()) {
                fixReturnType(child, returnType, declarationTable);
            }
        }
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
        if (ifStatement.getElseScope() == null)
            return false;

        return statementReturns(ifStatement.getIfScope()) && statementReturns(ifStatement.getElseScope());
    }

}
