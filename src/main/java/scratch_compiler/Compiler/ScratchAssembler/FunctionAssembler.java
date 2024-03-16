package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.ScratchFunction;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
import scratch_compiler.Blocks.StopThisScriptBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.ReturnStatement;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.FunctionArgumentField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;

public class FunctionAssembler {
    private final static String stackOffset = "StackOffset";

    public static FunctionDefinitionBlock assembleFunction(FunctionDeclaration functionDeclaration) {
        Function function = functionDeclaration.getFunction();
        FunctionDefinitionBlock functionDefinitionBlock = new FunctionDefinitionBlock(
                compilerFunctionToScratchFunction(function));

        VariableStackReference stack = new VariableStackReference();
        for (Variable argument : function.getArguments())
            stack.addVariable(new ScratchVariable(argument.getName(), false));
        functionDefinitionBlock.push(ScratchAssembler.compileScope(functionDeclaration.getScope(), stack, true));

        if (!functionDefinitionBlock.getStack().isFinished())
            functionDefinitionBlock
                    .push(StackAssembler.forceLength(new AdditionField(getStackOffset(), new NumberField(1))));

        return functionDefinitionBlock;
    }

    public static ScratchFunction compilerFunctionToScratchFunction(Function function) {
        String name = function.getName();
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(stackOffset);
        return new ScratchFunction(name, true, arguments);
    }

    public static BlockStack assembleReturnStatement(ReturnStatement returnStatement, VariableStackReference stack) {
        BlockStack returnStack = new BlockStack();

        returnStack.push(StackAssembler.assignValueToStack(
                getStackOffset(stack.getVariableIndex(new ScratchVariable(stackOffset, false))),
                ScratchAssembler.assembleExpression(returnStatement.getExpression(), stack, true)));

        int returnTypeSize = returnStatement.getExpression().getType().size();
        returnStack.push(StackAssembler.forceLength(getStackOffset(returnTypeSize)));

        returnStack.push(new StopThisScriptBlock());
        return returnStack;
    }

    public static ValueField getStackOffset() {
        return new FunctionArgumentField(stackOffset);
    }

    public static ValueField getStackOffset(int offset) {
        return StackAssembler.offsetIndex(getStackOffset(), offset);
    }
}
