package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.IfBlock;
import scratch_compiler.Blocks.IfElseBlock;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;

public class IfAssembler {
    public static Block assemble(IfStatement ifStatement, StackReference stack) {
        if (ifStatement.getElseStatement() == null)
            return IfAssembler.assembleIf(ifStatement, stack);
        else
            return IfAssembler.assembleIfElse(ifStatement, stack);
    }

    private static Block assembleIf(IfStatement ifStatement, StackReference stack) {
        IfBlock ifBlock = new IfBlock(new EqualsField(ScratchAssembler.assembleExpression(ifStatement.getExpression(), stack), new NumberField(1)));
        Block ifBody = ScratchAssembler.assembleStatement(ifStatement.getStatement(), stack);
        ifBlock.connectIf(ifBody);
        return ifBlock;
    }

    private static Block assembleIfElse(IfStatement ifStatement, StackReference stack) {
        IfElseBlock ifBlock = new IfElseBlock(new EqualsField(ScratchAssembler.assembleExpression(ifStatement.getExpression(), stack), new NumberField(1)));
        Block ifBody = ScratchAssembler.assembleStatement(ifStatement.getStatement(), stack);
        Block elseBody = ScratchAssembler.assembleStatement(ifStatement.getElseStatement(), stack);
        ifBlock.connectIf(ifBody);
        ifBlock.connectElse(elseBody);
        return ifBlock;
        
    }
}
