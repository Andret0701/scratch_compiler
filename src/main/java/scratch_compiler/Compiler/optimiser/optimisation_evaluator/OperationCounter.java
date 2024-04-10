package scratch_compiler.Compiler.optimiser.optimisation_evaluator;

import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.UnaryOperator;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class OperationCounter {
    public static int countOperations(IntermediateCode code) {
        int operations = 0;
        operations += countOperations(code.getGlobalScope());
        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            operations += countOperations(function.getScope());
        }
        return operations;
    }

    private static int countOperations(Scope scope) {
        int operations = 0;
        for (Statement statement : scope.getStatements()) {
            operations++;
            operations += countOperations(statement);
            for (int i = 0; i < statement.getScopeCount(); i++) {
                Scope innerScope = statement.getScope(i);
                operations += countOperations(innerScope);
            }
        }
        return operations;
    }

    private static int countOperations(ExpressionContainer container) {
        int operations = 0;
        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);
            if (expression instanceof BinaryOperator)
                operations++;
            else if (expression instanceof UnaryOperator)
                operations++;

            operations += countOperations(expression);
        }
        return operations;
    }
}
