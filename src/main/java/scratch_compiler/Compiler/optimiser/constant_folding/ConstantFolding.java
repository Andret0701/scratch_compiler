package scratch_compiler.Compiler.optimiser.constant_folding;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;

public class ConstantFolding implements Optimization {
    @Override
    public Optimized optimize(CompiledCode code) {
        return new Optimized(code, false);
    }
}
