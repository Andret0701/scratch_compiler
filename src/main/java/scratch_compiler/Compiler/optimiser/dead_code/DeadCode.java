package scratch_compiler.Compiler.optimiser.dead_code;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;

public class DeadCode implements Optimization {
    @Override
    public Optimized optimize(IntermediateCode code) {
        return new Optimized(code, false);
    }

}
