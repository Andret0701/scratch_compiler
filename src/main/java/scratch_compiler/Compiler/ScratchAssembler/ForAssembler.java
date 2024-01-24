package scratch_compiler.Compiler.ScratchAssembler;


import scratch_compiler.Blocks.RepeatUntilBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.parser.statements.ForStatement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;

public class ForAssembler {
    public static BlockStack assemble(ForStatement forStatement, StackReference stack) {
        stack.addScope();

        BlockStack forStack = new BlockStack();

        StackBlock declaration= ScratchAssembler.assembleDeclaration(forStatement.getDeclaration(), stack);
        forStack.push(declaration);

        RepeatUntilBlock forBlock = new RepeatUntilBlock(new EqualsField(ScratchAssembler.assembleExpression(forStatement.getExpression(), stack), new NumberField(0)));
        BlockStack forBody = ScratchAssembler.assembleStatement(forStatement.getStatement(), stack);
        forBlock.pushRepeat(forBody);
        
        StackBlock increment = ScratchAssembler.assembleAssignment(forStatement.getIncrement(), stack);
        forBlock.pushRepeat(increment); 

        forStack.push(forBlock);


        int numRemoved=stack.removeScope();
        for (int i = 0; i < numRemoved; i++)
            forStack.push(ScratchAssembler.getListRemoveLastBlock("Stack", false));
        return forStack;
    }
}
