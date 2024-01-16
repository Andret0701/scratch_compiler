package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.RepeatUntilBlock;
import scratch_compiler.Compiler.parser.statements.ForStatement;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;

public class ForAssembler {
    public static Block assemble(ForStatement forStatement, StackReference stack) {
        stack.addScope();
        Block declaration= ScratchAssembler.assembleDeclaration(forStatement.getDeclaration(), stack);

        RepeatUntilBlock forBlock = new RepeatUntilBlock(new EqualsField(ScratchAssembler.assembleExpression(forStatement.getExpression(), stack), new NumberField(0)));
        Block whileBody = ScratchAssembler.assembleStatement(forStatement.getStatement(), stack);
        Block increment = ScratchAssembler.assembleAssignment(forStatement.getIncrement(), stack);
        whileBody.addToStack(increment);   

        forBlock.connectRepeat(whileBody);
        declaration.addToStack(forBlock);

        int numRemoved=stack.removeScope();
        for (int i = 0; i < numRemoved; i++)
            declaration.addToStack(ScratchAssembler.getListRemoveLastBlock("Stack", false));
        return declaration;
    }
}
