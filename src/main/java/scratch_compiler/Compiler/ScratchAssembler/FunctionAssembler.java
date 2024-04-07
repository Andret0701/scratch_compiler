package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.ScratchFunction;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
import scratch_compiler.Blocks.StopThisScriptBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.ReturnStatement;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.FunctionArgumentField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;

public class FunctionAssembler {
    public static FunctionDefinitionBlock assembleFunction(SimpleFunctionDeclaration functionDeclaration) {
        FunctionDefinitionBlock functionDefinitionBlock = new FunctionDefinitionBlock(
                new ScratchFunction(functionDeclaration.getName(), true, new ArrayList<>()));

        BlockStack blockStack = ScopeAssembler.assemble(functionDeclaration.getScope());
        if (blockStack.size() != 0 && blockStack.get(blockStack.size() - 1) instanceof StopThisScriptBlock)
            blockStack.pop();

        functionDefinitionBlock.push(blockStack);
        return functionDefinitionBlock;
    }
}
