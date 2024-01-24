package scratch_compiler.Blocks;

import scratch_compiler.Function;
import scratch_compiler.Blocks.Types.Block;

public class FunctionDefinitionBlock extends Block{
    public FunctionDefinitionBlock(Function function) {
        super("procedures_definition");//, function);
    }
    
}