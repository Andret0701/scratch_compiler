package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.RepeatUntilBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.parser.statements.WhileStatement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;

public class WhileAssembler {
    public static StackBlock assemble(WhileStatement whileStatement, StackReference stack) {
        RepeatUntilBlock whileBlock = new RepeatUntilBlock(new EqualsField(ScratchAssembler.assembleExpression(whileStatement.getExpression(), stack), new NumberField(0)));
        BlockStack whileBody = ScratchAssembler.assembleStatement(whileStatement.getStatement(), stack);
        whileBlock.pushRepeat(whileBody);
        return whileBlock;
    }
}
