package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.Blocks.AddListBlock;
import scratch_compiler.Blocks.ChangeListBlock;
import scratch_compiler.Blocks.ChangeVariableBlock;
import scratch_compiler.Blocks.ClearListBlock;
import scratch_compiler.Blocks.LoopBlock;
import scratch_compiler.Blocks.RepeatUntilBlock;
import scratch_compiler.Blocks.SetVariableBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.ListElementField;
import scratch_compiler.ValueFields.ListLengthField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;
import scratch_compiler.ValueFields.LogicFields.LessThanField;

public class StackAssembler {
    private static String stackName = "Stack";
    private static String pointerName = "StackPointer";

    public static BlockStack initializeStack() {
        BlockStack stackBlock = new BlockStack();
        RepeatUntilBlock repeatUntilBlock = new RepeatUntilBlock(new EqualsField(new ListLengthField(stackName, false), new NumberField(200000)));
        repeatUntilBlock.pushRepeat(new AddListBlock(stackName,false, new NumberField(0)));
        stackBlock.push(repeatUntilBlock);
        stackBlock.push(new SetVariableBlock(pointerName, false, new NumberField(1)));
        return stackBlock;
    }


    public static BlockStack addValueToStack(ValueField value) {
        BlockStack stackBlock = new BlockStack();
        stackBlock.push(assignValueToStack(getPointer(), value));
        stackBlock.push(offsetPointer(1));
        return stackBlock;
    }

    public static BlockStack removeLastValueFromStack() {
        BlockStack stackBlock = new BlockStack();
        stackBlock.push(offsetPointer(-1));
        return stackBlock;
    }

    public static BlockStack assignValueToStack(ValueField index, ValueField value) {
        BlockStack stackBlock = new BlockStack();
        stackBlock.push(new ChangeListBlock(stackName, false, index, value));
        return stackBlock;
    }

    public static ValueField lengthOfStack() {
        return getPointer();
    }

    public static ValueField getElementOfStack(ValueField index) {
        return new ListElementField(stackName, false, index);
    }

    public static ValueField getPointer() {
        return new VariableField(pointerName, false);
    }

    public static BlockStack offsetPointer(int offset) {
        BlockStack stackBlock = new BlockStack();
        if (offset != 0)
            stackBlock.push(new ChangeVariableBlock(pointerName, false, new NumberField(offset)));
        return stackBlock;
    }

    public static BlockStack offsetPointer(ValueField offset) {
        BlockStack stackBlock = new BlockStack();
        stackBlock.push(new ChangeVariableBlock(pointerName, false, offset));
        return stackBlock;
    }

    public static BlockStack setPointer(ValueField value) {
        BlockStack stackBlock = new BlockStack();
        stackBlock.push(new SetVariableBlock(pointerName, false, value));
        return stackBlock;
    }

    public static ValueField offsetIndex(ValueField index, int offset) {
        if (offset == 0)
            return index;
        if (offset > 0)
            return new AdditionField(index, new NumberField(offset));
        return new SubtractionField(index, new NumberField(-offset));
    }

    public static BlockStack copyTo(ValueField fromIndex, ValueField toIndex) {
        BlockStack stackBlock = new BlockStack();
        stackBlock.push(assignValueToStack(toIndex, getElementOfStack(fromIndex)));
        return stackBlock;
    }


    public static BlockStack copyValues(ValueField toIndex, ArrayList<ValueField> valueIndices)
    {
        BlockStack stackBlock = new BlockStack();
        for (int i = 0; i < valueIndices.size(); i++)
            stackBlock.push(copyTo(valueIndices.get(i), offsetIndex(toIndex, i)));
        return stackBlock;
    }

    public static BlockStack copyValues(ValueField toIndex, ValueField fromIndex, int length)
    {
        BlockStack stackBlock = new BlockStack();
        for (int i = 0; i < length; i++)
            stackBlock.push(copyTo(offsetIndex(fromIndex, i), offsetIndex(toIndex, i)));
        return stackBlock;
    }


    public static BlockStack forceLength(ValueField length) {
        BlockStack stackBlock = new BlockStack();
        stackBlock.push(setPointer(length));
        return stackBlock;
    }

    public static BlockStack removeMultiple(int number)
    {
        BlockStack stackBlock = new BlockStack();
        for (int i = 0; i < number; i++)
            stackBlock.push(removeLastValueFromStack());
        return stackBlock;
    }
}
