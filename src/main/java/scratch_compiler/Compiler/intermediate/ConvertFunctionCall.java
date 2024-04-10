package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.intermediate.simple_code.Push;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.intermediate.simple_code.VariableReference;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.ScopeContainer;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.FunctionCallExpression;
import scratch_compiler.Compiler.parser.expressions.IndexExpression;
import scratch_compiler.Compiler.parser.expressions.ReferenceExpression;
import scratch_compiler.Compiler.parser.expressions.SizeOfExpression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertFunctionCall {
    public static ArrayList<Statement> convert(FunctionCall functionCall, IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (Expression argument : functionCall.getArguments().reversed()) {
            argument = ConvertVariableReference.convert(argument);
            statements.addAll(ConvertStack.push(argument, table));
        }

        String name = ConvertFunction.convertFunctionName(functionCall.getFunction());
        statements.add(new SimpleFunctionCall(name));

        // for (functionCall.getFunction().getReturnType())

        return statements;
    }

    public static Scope convert(Scope scope, IntermediateTable table) {
        Scope converted = new Scope();
        for (Statement statement : scope.getStatements()) {
            converted.addAllStatements(convert(statement, table));
        }
        return converted;
    }

    private static ArrayList<Statement> convert(Statement statement, IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (statement instanceof Scope) {
            statements.add(convert((Scope) statement, table));
            return statements;
        }

        statements.addAll(convert((ExpressionContainer) statement, table));
        statements.add(statement);

        for (int i = 0; i < statement.getScopeCount(); i++) {
            Scope scope = statement.getScope(i);
            statement.setScope(i, convert(scope, table));
        }

        return statements;
    }

    private static ArrayList<Statement> convert(ExpressionContainer container, IntermediateTable table) {

        ArrayList<Statement> statements = new ArrayList<>();
        if (container == null) {
            return statements;
        }

        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);
            statements.addAll(convert(expression, table));
            if (expression instanceof FunctionCallExpression) {
                FunctionCallExpression functionCall = (FunctionCallExpression) expression;

                String name = table.getUniqueTemp(functionCall.getFunction().getName());
                statements.addAll(convert(name, functionCall, table));
                container.setExpression(i,
                        new VariableReference(name, functionCall.getFunction().getReturnType(), null));
            }

        }

        return statements;

    }

    private static ArrayList<Statement> convert(String name, FunctionCallExpression functionCall,
            IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (Expression argument : functionCall.getArguments().reversed()) {
            argument = ConvertVariableReference.convert(argument);
            if (argument instanceof VariableValue || argument instanceof ReferenceExpression
                    || argument instanceof IndexExpression
                    || argument instanceof SizeOfExpression)
                throw new IllegalArgumentException("Function call is not allowed in intermediate code: " + argument);
            System.out.println("Function call is not allowed in intermediate code: " + argument);
            statements.addAll(ConvertStack.push(argument, table));
        }

        String functionName = ConvertFunction.convertFunctionName(functionCall.getFunction());
        statements.add(new SimpleFunctionCall(functionName));

        Type returnType = functionCall.getFunction().getReturnType();
        if (returnType.getType().getType() == VariableType.VOID)
            return statements;

        if (returnType.isArray()) {
            statements.addAll(ConvertDeclaration.declareVariable("size:" + name, new TypeDefinition(VariableType.INT)));
            statements.addAll(ConvertStack.popVariable("size:" + name, new TypeDefinition(VariableType.INT)));
            statements.addAll(ConvertDeclaration.declareArray(name, "size:" + name, returnType.getType()));
            statements.addAll(ConvertStack.popArray(name, "size:" + name, returnType.getType(), table));
        } else {
            statements.addAll(ConvertDeclaration.declareVariable(name, returnType.getType()));
            statements.addAll(ConvertStack.popVariable(name, returnType.getType()));
        }

        return statements;
    }
}
