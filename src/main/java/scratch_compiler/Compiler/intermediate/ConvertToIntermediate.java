package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.VariableReference;
import scratch_compiler.Compiler.intermediate.validator.IntermediateValidator;
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

        IntermediateCode convertedCode = new IntermediateCode(globalScope, convertedFunctions);
        // System.out.println("Converted code: " + convertedCode);
        IntermediateValidator.validateIntermediateCode(convertedCode);
        return convertedCode;
    }
}
