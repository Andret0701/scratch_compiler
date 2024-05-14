package scratch_compiler.Compiler.optimiser;

import java.util.ArrayList;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.optimiser.constant_folding.ConstantFolding;
import scratch_compiler.Compiler.optimiser.constant_folding.CopyConstants;
import scratch_compiler.Compiler.optimiser.function_inlining.FunctionInlining;
import scratch_compiler.Compiler.optimiser.optimisation_evaluator.OptimisationEvaluator;
import scratch_compiler.Compiler.optimiser.stack_optimizer.StackOptimizer;
import scratch_compiler.Compiler.optimiser.unreachable_code.UnreachableCode;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class Optimizer {
    private static ArrayList<Optimization> optimizations = new ArrayList<Optimization>() {
        {
            add(new UnreachableCode());
            add(new ConstantFolding());
            add(new CopyConstants());
            add(new FunctionInlining());
            add(new StackOptimizer());
        }
    };

    public static IntermediateCode optimize(IntermediateCode code) {
        return optimize(code, false);
    }

    public static IntermediateCode optimize(IntermediateCode code, boolean debug) {
        OptimisationEvaluator optimisationEvaluator = new OptimisationEvaluator(code);

        // if (debug)
        // System.out.println("Optimising...");
        // System.out.println(code);

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Optimization optimization : optimizations) {
                Optimized optimized = optimization.optimize(code);
                if (optimized.isOptimized()) {
                    code = (IntermediateCode) optimized.getObject();
                    // System.out.println(code);
                    changed = true;

                    // if (debug) {
                    // System.out.println("Optimised " + optimization.getClass().getSimpleName() +
                    // ":");
                    // optimisationEvaluator.evaluate(code, 3);
                    // }
                }
            }
        }

        if (debug) {
            System.out.println("Optimisation complete");
            optimisationEvaluator.totalEvaluate(code, 3);
        }

        return code;
    }

}
