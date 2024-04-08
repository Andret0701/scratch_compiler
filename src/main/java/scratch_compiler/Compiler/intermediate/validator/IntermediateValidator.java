package scratch_compiler.Compiler.intermediate.validator;

import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.intermediate.simple_code.VariableReference;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.FunctionCallExpression;
import scratch_compiler.Compiler.parser.expressions.IndexExpression;
import scratch_compiler.Compiler.parser.expressions.ReferenceExpression;
import scratch_compiler.Compiler.parser.expressions.SizeOfExpression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class IntermediateValidator {
    public static void validateIntermediateCode(IntermediateCode code) {
        SimpleDeclarationTable table = new SimpleDeclarationTable();
        validateConvertedScope(code.getGlobalScope(), table);
        for (SimpleFunctionDeclaration function : code.getFunctions())
            validateConvertedScope(function.getScope(), table);
    }

    private static void validateConvertedScope(Scope scope, SimpleDeclarationTable table) {
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof Assignment)
                throw new IllegalArgumentException(
                        "Assignmnet is not allowed in intermediate code. Use simple assignment instead: " + statement);
            if (statement instanceof VariableDeclaration)
                throw new IllegalArgumentException(
                        "Variable declaration is not allowed in intermediate code. Use simple declaration instead: "
                                + statement);
            if (statement instanceof FunctionCall)
                throw new IllegalArgumentException(
                        "Function call is not allowed in intermediate code. Use simple function call instead: "
                                + statement);

            if (statement instanceof SimpleVariableDeclaration) {
                SimpleVariableDeclaration declaration = (SimpleVariableDeclaration) statement;
                table.declareVariable(declaration.getName());
            }

            if (statement instanceof SimpleArrayDeclaration) {
                SimpleArrayDeclaration declaration = (SimpleArrayDeclaration) statement;
                table.declareArray(declaration.getName());
            }

            if (statement instanceof SimpleVariableAssignment) {
                SimpleVariableAssignment assignment = (SimpleVariableAssignment) statement;
                table.validateVariableUsage(assignment.getName());
            }

            if (statement instanceof SimpleArrayAssignment) {
                SimpleArrayAssignment assignment = (SimpleArrayAssignment) statement;
                table.validateArrayUsage(assignment.getName());
            }

            validateExpressionContainer(statement, table, scope.toString());
            for (int i = 0; i < statement.getScopeCount(); i++) {
                Scope innerScope = statement.getScope(i);
                if (innerScope == null)
                    continue;

                validateConvertedScope(innerScope, table.copy());
            }
        }
    }

    private static void validateExpressionContainer(ExpressionContainer container, SimpleDeclarationTable table,
            String found) {
        if (container == null)
            return;

        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);
            if (expression == null)
                continue;

            if (expression instanceof FunctionCallExpression)
                throw new IllegalArgumentException(
                        "Function call is not allowed in intermediate code: "
                                + expression + " " + found);

            if (expression instanceof VariableValue)
                throw new IllegalArgumentException(
                        "Variable value is not allowed in intermediate code: "
                                + expression + " " + found);

            if (expression instanceof SizeOfExpression)
                throw new IllegalArgumentException(
                        "Size of expression is not allowed in intermediate code: "
                                + expression + " " + found);

            if (expression instanceof IndexExpression)
                throw new IllegalArgumentException(
                        "Index expression is not allowed in intermediate code: "
                                + expression + " " + found);

            if (expression instanceof ReferenceExpression)
                throw new IllegalArgumentException(
                        "Reference expression is not allowed in intermediate code: "
                                + expression + " " + found);

            if (expression instanceof VariableReference)
                throw new IllegalArgumentException(
                        "Variable reference is not allowed in intermediate code: "
                                + expression + " " + found);

            if (expression instanceof SimpleVariableValue) {
                SimpleVariableValue value = (SimpleVariableValue) expression;
                table.validateVariableUsage(value.getName());
            }

            if (expression instanceof SimpleArrayValue) {
                SimpleArrayValue assignment = (SimpleArrayValue) expression;
                table.validateArrayUsage(assignment.getName());
            }

            validateExpressionContainer((ExpressionContainer) expression, table, found);
        }
    }
}
