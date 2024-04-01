package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;

public class ConvertToIntermediate {

    public static CompiledCode convert(CompiledCode code) {
        IntermediateTable table = new IntermediateTable();

        Scope globalScope = ConvertScope.convert(code.getGlobalScope(), table);
        ArrayList<SimpleFunctionDeclaration> convertedFunctions = new ArrayList<>();
        for (FunctionDeclaration function : code.getFunctions()) {
            SimpleFunctionDeclaration convertedFunction = ConvertFunction.convert(function, table);
            convertedFunctions.add(convertedFunction);
        }
        // CompiledCode convertedCode = new CompiledCode(globalScope,
        // convertedFunctions, code.getStructs());

        System.out.println("Converted code:");
        for (SimpleFunctionDeclaration function : convertedFunctions) {
            System.out.println(function);
        }
        System.out.println(globalScope);

        return null;
    }
}
