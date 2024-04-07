package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.ChangeListBlock;
import scratch_compiler.Blocks.ChangeVariableBlock;
import scratch_compiler.Blocks.SetVariableBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;

public class AssignmentAssembler {
    public static BlockStack assemble(SimpleVariableAssignment assignment) {
        BlockStack stackBlock = new BlockStack();

        ValueField value = ExpressionAssembler.assemble(assignment.getValue());
        if ((value instanceof AdditionField)) {
            AdditionField additionField = (AdditionField) value;
            if ((additionField.getLeft() instanceof VariableField)
                    && ((VariableField) additionField.getLeft()).getVariable().getName().equals(assignment.getName())) {
                stackBlock.push(new ChangeVariableBlock(assignment.getName(), false,
                        additionField.getRight()));
                return stackBlock;
            }
            if ((additionField.getRight() instanceof VariableField)
                    && ((VariableField) additionField.getRight()).getVariable().getName()
                            .equals(assignment.getName())) {
                stackBlock.push(new ChangeVariableBlock(assignment.getName(), false,
                        additionField.getLeft()));
                return stackBlock;
            }
        }

        // if value is variable + expression use change variable block instead of set
        // variable block
        stackBlock.push(new SetVariableBlock(assignment.getName(), false,
                value));
        return stackBlock;
    }

    public static BlockStack assemble(SimpleArrayAssignment assignment) {
        BlockStack stackBlock = new BlockStack();

        ValueField value = ExpressionAssembler.assemble(assignment.getValue());
        ValueField index = new AdditionField(ExpressionAssembler.assemble(assignment.getIndex()),
                new NumberField(1));
        stackBlock.push(new ChangeListBlock(assignment.getName(), false,
                index, value));
        return stackBlock;
    }
}
