package scratch_compiler.Compiler.ScratchAssembler;

import java.util.Stack;

import scratch_compiler.Blocks.IfBlock;
import scratch_compiler.Blocks.IfElseBlock;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;
import scratch_compiler.ValueFields.LogicFields.LogicField;

public class IfAssembler {
    public static StackBlock assemble(IfStatement ifStatement) {
        if (ifStatement.getElseScope() == null || ifStatement.getElseScope().getStatements().isEmpty())
            return IfAssembler.assembleIf(ifStatement);
        else
            return IfAssembler.assembleIfElse(ifStatement);
    }

    private static StackBlock assembleIf(IfStatement ifStatement) {
        ValueField expression = ExpressionAssembler.assemble(ifStatement.getExpression());
        if (!(expression instanceof LogicField))
            expression = new EqualsField(expression, new NumberField(1));

        IfBlock ifBlock = new IfBlock((LogicField) expression);
        BlockStack ifBody = ScopeAssembler.assemble(ifStatement.getIfScope());
        ifBlock.pushIf(ifBody);
        return ifBlock;
    }

    private static StackBlock assembleIfElse(IfStatement ifStatement) {
        ValueField expression = ExpressionAssembler.assemble(ifStatement.getExpression());
        if (!(expression instanceof LogicField))
            expression = new EqualsField(expression, new NumberField(1));

        IfElseBlock ifBlock = new IfElseBlock((LogicField) expression);
        BlockStack ifBody = ScopeAssembler.assemble(ifStatement.getIfScope());
        BlockStack elseBody = ScopeAssembler.assemble(ifStatement.getElseScope());
        ifBlock.pushIf(ifBody);
        ifBlock.pushElse(elseBody);
        return ifBlock;

    }
}
