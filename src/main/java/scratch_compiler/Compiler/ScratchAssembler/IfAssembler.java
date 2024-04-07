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
    public static StackBlock assemble(IfStatement ifStatement) {
        if (ifStatement.getElseScope() == null)
            return IfAssembler.assembleIf(ifStatement);
        else
            return IfAssembler.assembleIfElse(ifStatement);
    }

    private static StackBlock assembleIf(IfStatement ifStatement) {
        IfBlock ifBlock = new IfBlock(
                new EqualsField(ExpressionAssembler.assemble(ifStatement.getExpression()),
                        new NumberField(1)));
        BlockStack ifBody = ScopeAssembler.assemble(ifStatement.getIfScope());
        ifBlock.pushIf(ifBody);
        return ifBlock;
    }

    private static StackBlock assembleIfElse(IfStatement ifStatement) {
        IfElseBlock ifBlock = new IfElseBlock(
                new EqualsField(ExpressionAssembler.assemble(ifStatement.getExpression()),
                        new NumberField(1)));
        BlockStack ifBody = ScopeAssembler.assemble(ifStatement.getIfScope());
        BlockStack elseBody = ScopeAssembler.assemble(ifStatement.getElseScope());
        ifBlock.pushIf(ifBody);
        ifBlock.pushElse(elseBody);
        return ifBlock;

    }
}
