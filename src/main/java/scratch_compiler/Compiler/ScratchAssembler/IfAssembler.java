package scratch_compiler.Compiler.ScratchAssembler;

import java.util.Stack;

import scratch_compiler.Blocks.IfBlock;
import scratch_compiler.Blocks.IfElseBlock;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;

public class IfAssembler {
    public static StackBlock assemble(IfStatement ifStatement, VariableStackReference stack, boolean isFunction) {
        if (ifStatement.getElseStatement() == null)
            return IfAssembler.assembleIf(ifStatement, stack, isFunction);
        else
            return IfAssembler.assembleIfElse(ifStatement, stack, isFunction);
    }

    private static StackBlock assembleIf(IfStatement ifStatement, VariableStackReference stack, boolean isFunction) {
        IfBlock ifBlock = new IfBlock(new EqualsField(ScratchAssembler.assembleExpression(ifStatement.getExpression(), stack,isFunction), new NumberField(1)));
        BlockStack ifBody = ScratchAssembler.assembleStatement(ifStatement.getStatement(), stack,isFunction);
        ifBlock.pushIf(ifBody);
        return ifBlock;
    }

    private static StackBlock assembleIfElse(IfStatement ifStatement, VariableStackReference stack, boolean isFunction) {
        IfElseBlock ifBlock = new IfElseBlock(new EqualsField(ScratchAssembler.assembleExpression(ifStatement.getExpression(), stack,isFunction), new NumberField(1)));
        BlockStack ifBody = ScratchAssembler.assembleStatement(ifStatement.getStatement(), stack,isFunction);
        BlockStack elseBody = ScratchAssembler.assembleStatement(ifStatement.getElseStatement(), stack,isFunction);
        ifBlock.pushIf(ifBody);
        ifBlock.pushElse(elseBody);
        return ifBlock;
        
    }
}
