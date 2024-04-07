package scratch_compiler.Compiler.scratchIntermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ConvertToScratchIntermediate {
    public static IntermediateCode convert(IntermediateCode code) {
        Scope globalScope = convertGlobaScope(code.getGlobalScope());
        ArrayList<SimpleFunctionDeclaration> functions = new ArrayList<>();
        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            functions.add(new SimpleFunctionDeclaration(function.getName(), ConvertScope.convert(function.getScope())));
        }

        return new IntermediateCode(globalScope, functions);
    }

    private static Scope convertGlobaScope(Scope scope) {
        Scope temp = ConvertScope.convert(scope);
        Scope converted = new Scope();
        converted.addStatement(new SimpleVariableDeclaration("stack:pointer", VariableType.INT));
        converted.addStatement(new SimpleVariableAssignment("stack:pointer", new IntValue(0)));
        converted.addStatement(new SimpleArrayDeclaration("stack:stack", VariableType.INT, new IntValue(200000)));
        converted.addAllStatements(temp.getStatements());
        return converted;
    }
}
