package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.ScratchFunction;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.FunctionCallBlock;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.ValueFields.ValueField;

public class FunctionCallAssembler {
    public static BlockStack assemble(SimpleFunctionCall functionCall) {

        ScratchFunction function = new ScratchFunction(functionCall.getName(), true, new ArrayList<>());

        BlockStack stackBlock = new BlockStack();
        stackBlock.push(new FunctionCallBlock(function, new ArrayList<>()));
        return stackBlock;
    }

}
