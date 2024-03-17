package scratch_compiler.Compiler.optimiser;

import scratch_compiler.Compiler.CompiledCode;

public interface Optimization {
    Optimized optimize(CompiledCode code);
}
