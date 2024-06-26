package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.IndexExpression;
import scratch_compiler.Compiler.parser.expressions.SizeOfExpression;
import scratch_compiler.Compiler.parser.expressions.types.Associativity;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.ErrorStatement;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.ReturnStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ConvertStatement {
    public static ArrayList<Statement> convert(Statement statement, IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        // statements.addAll(getIndexCheck(statement));
        // if (statement instanceof VariableDeclaration)
        // statements.addAll(ConvertDeclaration.convert((VariableDeclaration)
        // statement));
        // else

        // statements.addAll(ConvertFunctionCall.convert(statement, table));

        if (statement instanceof FunctionCall)
            statements.addAll(ConvertFunctionCall.convert((FunctionCall) statement,
                    table));
        else if (statement instanceof ReturnStatement)
            statements.addAll(ConvertReturn.convert((ReturnStatement) statement, table));
        else if (statement instanceof VariableDeclaration)
            statements.addAll(ConvertDeclaration.convert((VariableDeclaration) statement));
        else if (statement instanceof Assignment)
            statements.addAll(ConvertAssignment.convert((Assignment) statement));
        else if (statement instanceof Scope)
            statements.add(ConvertScope.convert((Scope) statement, table));
        else
            statements.add(statement);

        for (Statement converted : statements) {
            for (int i = 0; i < converted.getExpressionCount(); i++) {
                Expression expression = ConvertExpression.convert(converted.getExpression(i),
                        table);
                converted.setExpression(i, expression);
            }
        }

        for (int i = 0; i < statement.getScopeCount(); i++) {
            Scope scope = statement.getScope(i);
            statement.setScope(i, ConvertScope.convert(scope, table));
        }

        return statements;
    }

    private static ArrayList<Statement> getIndexCheck(Statement statement) {
        ArrayList<IndexExpression> indexed = ConvertExpression.getIndexed(statement);
        ArrayList<Statement> statements = new ArrayList<>();

        if (indexed.size() == 0)
            return statements;

        Expression check = getIndexCheck(indexed.get(0));
        for (int i = 1; i < indexed.size(); i++) {
            check = new BinaryOperator(OperatorType.OR, check, getIndexCheck(indexed.get(i)),
                    new Type(VariableType.BOOL));
        }

        ErrorStatement error = new ErrorStatement("Index out of bounds");
        IfStatement ifStatement = new IfStatement(check, error);
        statements.add(ifStatement);
        return statements;
    }

    // this uses the assumption that index must contain variable or functioncall
    private static Expression getIndexCheck(IndexExpression index) {
        BinaryOperator bottomCheck = new BinaryOperator(OperatorType.GREATER_THAN, new IntValue(0),
                index,
                new Type(VariableType.BOOL));
        BinaryOperator topCheck = new BinaryOperator(OperatorType.LESS_EQUALS,
                index,
                ConvertExpression.convertSize(new SizeOfExpression(index.getArray())),
                new Type(VariableType.BOOL));
        return new BinaryOperator(OperatorType.OR, bottomCheck, topCheck, new Type(VariableType.BOOL));
    }
}
