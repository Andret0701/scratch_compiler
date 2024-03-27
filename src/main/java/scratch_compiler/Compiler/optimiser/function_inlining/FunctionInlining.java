package scratch_compiler.Compiler.optimiser.function_inlining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.ReturnStatement;
import scratch_compiler.Compiler.parser.statements.Statement;

public class FunctionInlining implements Optimization {
    @Override
    public Optimized optimize(CompiledCode code) {

        Set<FunctionDeclaration> functionsToInline = getFunctionsToInline(code.getFunctions());
        boolean changed = functionsToInline.size() > 0;

        return new Optimized(code, changed);
    }

    private Set<FunctionDeclaration> getFunctionsToInline(ArrayList<FunctionDeclaration> functions) {
        Set<FunctionDeclaration> functionsToInline = new HashSet<FunctionDeclaration>();
        for (FunctionDeclaration function : functions) {
            if (function.getScope().getStatements().size() != 1)
                continue;

            Statement statement = function.getScope().getStatements().get(0);
            if (!(statement instanceof ReturnStatement))
                continue;

            ReturnStatement returnStatement = (ReturnStatement) statement;
            if (returnStatement.getExpression() == null)
                continue;

            functionsToInline.add(function);
        }

        return functionsToInline;
    }
}
