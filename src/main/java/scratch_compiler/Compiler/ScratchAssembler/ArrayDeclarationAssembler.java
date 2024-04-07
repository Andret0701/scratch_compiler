package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.AddListBlock;
import scratch_compiler.Blocks.RepeatUntilBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.ListLengthField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.LogicFields.GreaterThanField;
import scratch_compiler.ValueFields.LogicFields.LessThanField;

public class ArrayDeclarationAssembler {
    public static BlockStack assemble(SimpleArrayDeclaration arrayDeclaration) {
        BlockStack blockStack = new BlockStack();
        RepeatUntilBlock repeatUntilBlock = new RepeatUntilBlock(
                new GreaterThanField(
                        new AdditionField(new ListLengthField(arrayDeclaration.getName(), false), new NumberField(1)),
                        ExpressionAssembler.assemble(arrayDeclaration.getSize())));
        repeatUntilBlock.pushRepeat(new AddListBlock(arrayDeclaration.getName(), false, new NumberField(0)));
        blockStack.push(repeatUntilBlock);
        return blockStack;
    }
}
