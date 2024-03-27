package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;

public class ConvertToIntermediate {

    public static CompiledCode convert(CompiledCode code) {
        Scope globalScope = ConvertScope.convertScope(code.getGlobalScope());
        ArrayList<FunctionDeclaration> convertedFunctions = new ArrayList<>();
        for (FunctionDeclaration function : code.getFunctions()) {
            System.out.println("Converting function: " + function.getFunction().getName());
            Scope functionScope = ConvertScope.convertScope(function.getScope());
            FunctionDeclaration convertedFunction = new FunctionDeclaration(function.getFunction(), functionScope);
            convertedFunctions.add(convertedFunction);
        }
        CompiledCode convertedCode = new CompiledCode(globalScope, convertedFunctions, code.getStructs());

        return convertedCode;
    }
}
