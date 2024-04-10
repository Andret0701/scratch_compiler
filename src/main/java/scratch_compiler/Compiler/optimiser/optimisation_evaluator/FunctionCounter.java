package scratch_compiler.Compiler.optimiser.optimisation_evaluator;

import scratch_compiler.Compiler.intermediate.IntermediateCode;

public class FunctionCounter {
    public static int countFunctions(IntermediateCode code) {
        return code.getFunctions().size();
    }
}
