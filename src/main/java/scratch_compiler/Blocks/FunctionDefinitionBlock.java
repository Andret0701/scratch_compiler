package scratch_compiler.Blocks;

import scratch_compiler.ScratchFunction;
import scratch_compiler.Blocks.Types.HatBlock;

public class FunctionDefinitionBlock extends HatBlock{
    private ScratchFunction function;
    public FunctionDefinitionBlock(ScratchFunction function) {
        super("procedures_definition");
        this.function = function;
    }

    public ScratchFunction getFunction() {
        return function;
    }    
}