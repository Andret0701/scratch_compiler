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
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class ConvertToScratchIntermediate {
    public static IntermediateCode convert(IntermediateCode code) {
        Scope globalScope = convertGlobaScope(code.getGlobalScope(), code.getFunctions());
        ArrayList<SimpleFunctionDeclaration> functions = new ArrayList<>();
        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            functions.add(ConvertFunction.convert(function));
        }

        code = new IntermediateCode(globalScope, functions);
        return ConvertOneIndexed.convert(code);
    }

    private static Scope convertGlobaScope(Scope scope, ArrayList<SimpleFunctionDeclaration> functions) {
        Scope temp = ConvertScope.convert(scope);
        Scope converted = new Scope();
        converted.addAllStatements(declareStack("stack:stack", "stack:pointer"));

        for (SimpleFunctionDeclaration function : functions) {
            converted.addAllStatements(ConvertFunction.declareFunctionVariables(function));
        }

        converted.addAllStatements(temp.getStatements());
        return converted;
    }

    private static ArrayList<Statement> declareStack(String stackName, String pointerName) {
        ArrayList<Statement> statements = new ArrayList<>();
        statements.add(new SimpleVariableDeclaration(pointerName, VariableType.INT));
        statements.add(new SimpleVariableAssignment(pointerName, new IntValue(0)));
        statements.add(new SimpleArrayDeclaration(stackName, VariableType.INT, new IntValue(200000)));
        return statements;
    }
}
