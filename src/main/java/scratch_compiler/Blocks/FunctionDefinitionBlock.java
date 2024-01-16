package scratch_compiler.Blocks;

import scratch_compiler.Function;

public class FunctionDefinitionBlock extends Block{
    public FunctionDefinitionBlock(Function function) {
        super("procedures_definition", function);
    }
    
}