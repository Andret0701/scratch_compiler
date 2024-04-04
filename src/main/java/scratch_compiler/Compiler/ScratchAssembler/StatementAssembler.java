package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.ValueFields.StringField;

public class StatementAssembler {
    public static BlockStack assemble(Statement statement) {
        BlockStack stackBlock = new BlockStack();
        if (statement instanceof Scope) {
            Scope scope = (Scope) statement;
            stackBlock.push(ScopeAssembler.assemble(scope));
        } else if (statement instanceof SimpleVariableAssignment)
            stackBlock.push(AssignmentAssembler.assemble((SimpleVariableAssignment) statement));
        else if (statement instanceof SimpleArrayDeclaration) {

        } else if (statement instanceof SimpleVariableDeclaration) {

        } else
            stackBlock.push(errorBlock(statement));

        return stackBlock;
    }

    private static StackBlock errorBlock(Statement statement) {
        return new SayBlock("Compiler Error: " + statement.toString());
    }
}
