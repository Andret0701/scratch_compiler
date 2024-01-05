package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.RepeatUntilBlock;
import scratch_compiler.Compiler.parser.statements.WhileStatement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;
import scratch_compiler.ValueFields.LogicFields.NotField;

public class WhileAssembler {
    public static Block assemble(WhileStatement whileStatement, StackReference stack) {
        RepeatUntilBlock whileBlock = new RepeatUntilBlock(new EqualsField(ScratchAssembler.assembleExpression(whileStatement.getExpression(), stack), new NumberField(0)));
        Block whileBody = ScratchAssembler.assembleStatement(whileStatement.getStatement(), stack);
        whileBlock.connectRepeat(whileBody);
        return whileBlock;
    }
}
