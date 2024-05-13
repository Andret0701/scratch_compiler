package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.ScratchProgram;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.HatBlock;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.optimiser.Optimizer;
import scratch_compiler.Compiler.Compiler;
import scratch_compiler.Compiler.scratchIntermediate.ConvertToScratchIntermediate;

public class ScratchAssembler {
    public static ScratchProgram assemble(IntermediateCode code, boolean optimize) {
        code = ConvertToScratchIntermediate.convert(code);
        if (optimize)
            code = Optimizer.optimize(code);

        BlockStack blockStack = new BlockStack();
        blockStack.push(ScopeAssembler.assemble(code.getGlobalScope()));

        ArrayList<HatBlock> functionDefinitionBlocks = new ArrayList<>();
        for (SimpleFunctionDeclaration functionDeclaration : code.getFunctions()) {
            FunctionDefinitionBlock functionDefinitionBlock = FunctionAssembler.assembleFunction(functionDeclaration);
            functionDefinitionBlocks.add(functionDefinitionBlock);
        }

        return new ScratchProgram(blockStack, functionDefinitionBlocks);
    }

}
