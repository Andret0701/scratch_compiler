package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
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
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ConvertToIntermediate {

    public static IntermediateCode convert(CompiledCode code) {
        IntermediateTable table = new IntermediateTable();

        Scope globalScope = ConvertScope.convert(code.getGlobalScope(), table);
        ArrayList<SimpleFunctionDeclaration> convertedFunctions = new ArrayList<>();
        for (FunctionDeclaration function : code.getFunctions()) {
            SimpleFunctionDeclaration convertedFunction = ConvertFunction.convert(function, table);
            convertedFunctions.add(convertedFunction);
        }
        // CompiledCode convertedCode = new CompiledCode(globalScope,
        // convertedFunctions, code.getStructs());

        validateConvertedScope(globalScope);
        for (SimpleFunctionDeclaration function : convertedFunctions) {
            validateConvertedScope(function.getScope());
        }

        return new IntermediateCode(globalScope, convertedFunctions);
    }

    private static void validateConvertedScope(Scope scope) {
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof Assignment)
                throw new IllegalArgumentException(
                        "Assignmnet is not allowed in intermediate code. Use simple assignment instead: " + statement
                                + " " + scope);
            if (statement instanceof VariableDeclaration)
                throw new IllegalArgumentException(
                        "Variable declaration is not allowed in intermediate code. Use simple declaration instead: "
                                + statement);
            if (statement instanceof FunctionCall)
                throw new IllegalArgumentException(
                        "Function call is not allowed in intermediate code. Use simple function call instead: "
                                + statement);
            if (statement instanceof Scope)
                validateConvertedScope((Scope) statement);

            validateExpressionContainer(statement, statement.toString());
        }
    }

    private static void validateExpressionContainer(ExpressionContainer container, String found) {
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

            validateExpressionContainer((ExpressionContainer) expression, found);
        }
    }
}
