package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.ChangeVariableBlock;
import scratch_compiler.Blocks.SetVariableBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.ValueFields.ValueField;

public class AssignmentAssembler {
    public static BlockStack assemble(SimpleVariableAssignment assignment) {
        BlockStack stackBlock = new BlockStack();

        ValueField value = ExpressionAssembler.assemble(assignment.getValue());
        // if value is variable + expression use change variable block instead of set
        // variable block
        stackBlock.push(new SetVariableBlock(assignment.getName(), false,
                value));
        return stackBlock;
    }
}
