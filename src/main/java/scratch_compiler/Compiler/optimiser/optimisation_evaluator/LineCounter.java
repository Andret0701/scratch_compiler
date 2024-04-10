package scratch_compiler.Compiler.optimiser.optimisation_evaluator;

import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class LineCounter {
    public static int countLines(IntermediateCode code) {
        int lines = 0;
        lines += countLines(code.getGlobalScope());
        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            lines += countLines(function.getScope());
        }
        return lines;
    }

    private static int countLines(Scope scope) {
        int lines = 0;
        for (Statement statement : scope.getStatements()) {
            lines++;
            for (int i = 0; i < statement.getScopeCount(); i++) {
                Scope innerScope = statement.getScope(i);
                lines += countLines(innerScope);
            }
        }
        return lines;
    }
}
