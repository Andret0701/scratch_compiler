package scratch_compiler.Compiler.optimiser;

import java.util.ArrayList;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.optimiser.constant_folding.ConstantFolding;
import scratch_compiler.Compiler.optimiser.constant_folding.CopyConstants;
import scratch_compiler.Compiler.optimiser.unreachable_code.UnreachableCode;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class Optimizer {
    private static ArrayList<Optimization> optimizations = new ArrayList<Optimization>() {
        {
            add(new UnreachableCode());
            add(new ConstantFolding());
            add(new CopyConstants());
        }
    };

    public static IntermediateCode optimize(IntermediateCode code) {

        int lines = getNumberOfLines(code);
        System.out.println("Optimising...");
        System.out.println(code);

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Optimization optimization : optimizations) {
                Optimized optimized = optimization.optimize(code);
                if (optimized.isOptimized()) {
                    code = (IntermediateCode) optimized.getObject();
                    System.out.println("Optimised " + optimization.getClass().getSimpleName() + ":");
                    // System.out.println(code);
                    changed = true;

                    int currentLines = getNumberOfLines(code);
                    System.out.println(
                            lines + " -> " + currentLines + " lines: " + (lines - currentLines) + " lines removed");
                    lines = currentLines;
                }
            }
        }

        return code;
    }

    private static int getNumberOfLines(IntermediateCode code) {
        int lines = 0;
        lines += getNumberOfLines(code.getGlobalScope());
        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            lines += getNumberOfLines(function.getScope());
        }
        return lines;
    }

    private static int getNumberOfLines(Scope scope) {
        int lines = 0;
        for (Statement statement : scope.getStatements()) {
            lines++;
            for (int i = 0; i < statement.getScopeCount(); i++) {
                Scope innerScope = statement.getScope(i);
                lines += getNumberOfLines(innerScope);
            }
        }
        return lines;
    }
}
