package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.intermediate.simple_code.Push;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.FunctionCallExpression;
import scratch_compiler.Compiler.parser.expressions.IndexExpression;
import scratch_compiler.Compiler.parser.expressions.ReferenceExpression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertFunctionCall {
    public static ArrayList<Statement> convert(FunctionCall functionCall, IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (Expression argument : functionCall.getArguments().reversed()) {
            argument = ConvertExpression.convert(argument, table);
            statements.addAll(ConvertStack.push(argument, table));
        }

        statements.add(new SimpleFunctionCall(functionCall.getFunction().getName()));

        // for (functionCall.getFunction().getReturnType())

        return statements;
    }

    public static ArrayList<Statement> convert(ExpressionContainer container, IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        if (container == null) {
            return statements;
        }

        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);

            String referenceName = "";
            Expression index = null;

            if (expression instanceof IndexExpression) {
                IndexExpression indexExpression = (IndexExpression) expression;
                statements.addAll(ConvertFunctionCall.convert(indexExpression, table));

                referenceName = indexExpression.getArray().toString();
                index = indexExpression.getIndex();
                expression = indexExpression.getArray();
            }

            if (expression instanceof ReferenceExpression) {
                ReferenceExpression reference = (ReferenceExpression) expression;
                referenceName = ":" + reference.getReference();
                while (reference.getExpression() instanceof ReferenceExpression) {
                    reference = (ReferenceExpression) reference.getExpression();
                    referenceName = ":" + reference.getReference()
                            + referenceName;
                }
            }

            statements.addAll(ConvertFunctionCall.convert(expression, table));
            if (expression instanceof FunctionCallExpression) {
                FunctionCallExpression functionCall = (FunctionCallExpression) expression;

                String name = table.getUniqueTemp(functionCall.getFunction().getName());
                statements.addAll(convert(name, functionCall, table));
                if (functionCall.getType().getType().getType() == VariableType.STRUCT)
                    container.setExpression(i, new VariableValue(name, functionCall.getType()));
                else if (functionCall.getType().isArray())
                    container.setExpression(i,
                            new SimpleArrayValue(name, functionCall.getType().getType().getType(), functionCall));
                else
                    container.setExpression(i,
                            new SimpleVariableValue(name, functionCall.getType().getType().getType()));
            }

        }

        return statements;
    }

    private static ArrayList<Statement> convert(String name, FunctionCallExpression functionCall,
            IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (Expression argument : functionCall.getArguments().reversed()) {
            // argument = ConvertExpression.convert(argument, table);
            statements.addAll(ConvertStack.push(argument, table));
        }

        statements.add(new SimpleFunctionCall(functionCall.getFunction().getName()));

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
