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
    public static ScratchProgram assemble(String code) {
        IntermediateCode intermediateCode = Compiler.compile(code, ScratchCoreAssembler.getSystemCalls(), true);
        // System.out.println(intermediateCode);
        intermediateCode = ConvertToScratchIntermediate.convert(intermediateCode);
        // System.out.println(intermediateCode);
        /// throw new UnsupportedOperationException("Not implemented");

        intermediateCode = Optimizer.optimize(intermediateCode);
        System.out.println(intermediateCode);

        // ArrayList<FunctionDeclaration> functionDeclarations =
        // compiledCode.getFunctions();

        BlockStack blockStack = new BlockStack();
        blockStack.push(ScopeAssembler.assemble(intermediateCode.getGlobalScope()));
        // blockStack.push(StackAssembler.initializeStack());

        ArrayList<HatBlock> functionDefinitionBlocks = new ArrayList<>();
        for (SimpleFunctionDeclaration functionDeclaration : intermediateCode.getFunctions()) {
            FunctionDefinitionBlock functionDefinitionBlock = FunctionAssembler.assembleFunction(functionDeclaration);
            functionDefinitionBlocks.add(functionDefinitionBlock);
        }

        return new ScratchProgram(blockStack, functionDefinitionBlocks);
    }

}
