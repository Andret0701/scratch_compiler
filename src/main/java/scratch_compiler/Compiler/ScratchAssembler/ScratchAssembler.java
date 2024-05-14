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
        return assemble(code, optimize, false);
    }

    public static ScratchProgram assemble(IntermediateCode code, boolean optimize, boolean debug) {
        code = ConvertToScratchIntermediate.convert(code);
        if (debug) {
            System.out.println("Conversion to scratch intermediate code complete");
            System.out.println("   " + code.getFunctions().size() + " functions found");
            System.out.println("   " + code.getGlobalScope().getStatements().size() + " global statements found");
        }

        if (optimize)
            code = Optimizer.optimize(code, debug);

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
