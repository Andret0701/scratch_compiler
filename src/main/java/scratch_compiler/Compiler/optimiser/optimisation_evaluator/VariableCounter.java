package scratch_compiler.Compiler.optimiser.optimisation_evaluator;

import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class VariableCounter {
    public static int countVariables(IntermediateCode code) {
        int variables = 0;
        variables += countVariables(code.getGlobalScope());
        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            variables += countVariables(function.getScope());
        }
        return variables;
    }

    private static int countVariables(Scope scope) {
        int variables = 0;
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleVariableDeclaration)
                variables++;
            if (statement instanceof SimpleArrayDeclaration)
                variables++;

            for (int i = 0; i < statement.getScopeCount(); i++) {
                Scope innerScope = statement.getScope(i);
                variables += countVariables(innerScope);
            }
        }
        return variables;
    }

}
