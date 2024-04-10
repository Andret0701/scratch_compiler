package scratch_compiler.Compiler.intermediate;

import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.intermediate.simple_code.ArrayPop;
import scratch_compiler.Compiler.intermediate.simple_code.Pop;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleReturn;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.SizeOfExpression;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.WhileStatement;

public class ConvertFunction {

    public static SimpleFunctionDeclaration convert(FunctionDeclaration function, IntermediateTable table) {
        function.setScope(ConvertVariableReference.convert(function.getScope()));
        System.out.println("Function: " + function.getScope());
        Scope scope = convertFunctionScope(function, table);
        String name = convertFunctionName(function.getFunction());
        return new SimpleFunctionDeclaration(name, scope);
    }

    private static Scope convertFunctionScope(FunctionDeclaration functionDeclaration, IntermediateTable table) {
        Scope converted = new Scope();

        // add arguments to the scope
        for (Variable argument : functionDeclaration.getFunction().getArguments()) {
            boolean isArray = argument.getType().isArray();
            TypeDefinition type = argument.getType().getType();
            String name = convertArgumentName(argument);
            if (isArray) {
                converted.addAllStatements(
                        ConvertDeclaration.declareVariable("size:" + name, new TypeDefinition(VariableType.INT)));
                converted.addAllStatements(
                        ConvertStack.popVariable("size:" + name, new TypeDefinition(VariableType.INT)));
                converted.addAllStatements(
                        ConvertDeclaration.declareArray(name, "size:" + name, type));
                converted.addAllStatements(ConvertStack.popArray(name, "size:" + name, type, table));
            } else {
                converted.addAllStatements(ConvertDeclaration.declareVariable(name, type));
                converted.addAllStatements(ConvertStack.popVariable(name, type));
            }
        }

        // add statements to the scope
        for (Statement statement : ConvertScope.convert(functionDeclaration.getScope(), table).getStatements()) {
            converted.addStatement(statement);
        }

        if (!(converted.getStatements().get(converted.getStatements().size() - 1) instanceof SimpleReturn))
            converted
                    .addStatement(new SimpleReturn());

        return converted;
    }

    public static String convertFunctionName(Function function) {
        String name = "function:" + function.getName();
        for (Variable argument : function.getArguments())
            name += "." + argument.getType();
        return name;
    }

    public static String convertArgumentName(Variable argument) {
        return argument.getName();
    }
}
