package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.PenClearBlock;
import scratch_compiler.Blocks.PenDownBlock;
import scratch_compiler.Blocks.PenUpBlock;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.SetPositionBlock;
import scratch_compiler.Blocks.WaitBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Compiler.SystemCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.parser.expressions.SystemCallExpression;
import scratch_compiler.Compiler.parser.statements.SystemCallStatement;
import scratch_compiler.ValueFields.CosField;
import scratch_compiler.ValueFields.SinField;
import scratch_compiler.ValueFields.SqrtField;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.LogicFields.GetKetField;

public class SystemCallAssembler {

    public static BlockStack assemble(SystemCallStatement systemCall) {
        BlockStack stackBlock = new BlockStack();

        String name = systemCall.getSystemCall().getName();
        if (name.equals("say"))
            stackBlock.push(new SayBlock(ExpressionAssembler.assemble(systemCall.getArguments().get(0))));
        else if (name.equals("wait"))
            stackBlock.push(new WaitBlock(ExpressionAssembler.assemble(systemCall.getArguments().get(0))));
        else if (name.equals("penUp"))
            stackBlock.push(new PenUpBlock());
        else if (name.equals("penDown"))
            stackBlock.push(new PenDownBlock());
        else if (name.equals("penClear"))
            stackBlock.push(new PenClearBlock());
        else if (name.equals("moveTo"))
            stackBlock.push(new SetPositionBlock(ExpressionAssembler.assemble(systemCall.getArguments().get(0)),
                    ExpressionAssembler.assemble(systemCall.getArguments().get(1))));
        else
            stackBlock.push(StatementAssembler.errorBlock(systemCall));

        return stackBlock;
    }

    public static ValueField assemble(SystemCallExpression expression) {
        String name = expression.getSystemCall().getName();
        if (name.equals("sin"))
            return new SinField(ExpressionAssembler.assemble(expression.getArguments().get(0)));
        if (name.equals("cos"))
            return new CosField(ExpressionAssembler.assemble(expression.getArguments().get(0)));
        if (name.equals("sqrt"))
            return new SqrtField(ExpressionAssembler.assemble(expression.getArguments().get(0)));
        if (name.equals("getKey"))
            return new GetKetField(ExpressionAssembler.assemble(expression.getArguments().get(0)));

        return ExpressionAssembler.errorField(expression);
    }
}
