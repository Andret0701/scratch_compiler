package scratch_compiler.Compiler.ScratchAssembler;


import scratch_compiler.Blocks.RepeatUntilBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.parser.statements.ForStatement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;

public class ForAssembler {
    public static BlockStack assemble(ForStatement forStatement, VariableStackReference stack, boolean isFunction) {
        stack.addScope();

        BlockStack forStack = new BlockStack();

        BlockStack declaration= ScratchAssembler.assembleDeclaration(forStatement.getDeclaration(), stack, isFunction);
        forStack.push(declaration);

        RepeatUntilBlock forBlock = new RepeatUntilBlock(new EqualsField(ScratchAssembler.assembleExpression(forStatement.getExpression(), stack,isFunction), new NumberField(0)));
        BlockStack forBody = ScratchAssembler.assembleStatement(forStatement.getStatement(), stack, isFunction);
        forBlock.pushRepeat(forBody);
        
        BlockStack increment = ScratchAssembler.assembleAssignment(forStatement.getIncrement(), stack, isFunction);
        forBlock.pushRepeat(increment); 

        forStack.push(forBlock);


        int numRemoved=stack.removeScope();
        for (int i = 0; i < numRemoved; i++)
            forStack.push(StackAssembler.removeLastValueFromStack());
        return forStack;
    }
}
