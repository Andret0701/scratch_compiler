package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.LoopForeverBlock;
import scratch_compiler.Blocks.RepeatUntilBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.WhileStatement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;

public class WhileAssembler {
    public static StackBlock assemble(WhileStatement whileStatement) {
        ValueField condition = ExpressionAssembler.assemble(whileStatement.getExpression());
        if (condition instanceof NumberField) {
            NumberField numberField = (NumberField) condition;
            if (Double.parseDouble(numberField.getValue()) == 1) {
                LoopForeverBlock loopForeverBlock = new LoopForeverBlock();
                BlockStack loopBody = ScopeAssembler.assemble(whileStatement.getScope());
                loopForeverBlock.pushInside(loopBody);
                return loopForeverBlock;
            }
        }

        RepeatUntilBlock whileBlock = new RepeatUntilBlock(
                new EqualsField(condition,
                        new NumberField(0)));
        BlockStack whileBody = ScopeAssembler.assemble(whileStatement.getScope());
        whileBlock.pushRepeat(whileBody);
        return whileBlock;
    }
}
