package scratch_compiler.Compiler.optimiser;

import scratch_compiler.Compiler.intermediate.IntermediateCode;

public interface Optimization {
    Optimized optimize(IntermediateCode code);
}
