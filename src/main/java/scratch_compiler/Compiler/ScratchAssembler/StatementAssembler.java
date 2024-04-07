package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.StopThisScriptBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleReturn;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.SystemCallStatement;
import scratch_compiler.Compiler.parser.statements.WhileStatement;
import scratch_compiler.ValueFields.StringField;

public class StatementAssembler {
    public static BlockStack assemble(Statement statement) {
        BlockStack stackBlock = new BlockStack();
        if (statement instanceof Scope) {
            Scope scope = (Scope) statement;
            stackBlock.push(ScopeAssembler.assemble(scope));
        } else if (statement instanceof SimpleVariableAssignment)
            stackBlock.push(AssignmentAssembler.assemble((SimpleVariableAssignment) statement));
        else if (statement instanceof SimpleArrayAssignment)
            stackBlock.push(AssignmentAssembler.assemble((SimpleArrayAssignment) statement));
        else if (statement instanceof IfStatement)
            stackBlock.push(IfAssembler.assemble((IfStatement) statement));
        else if (statement instanceof WhileStatement)
            stackBlock.push(WhileAssembler.assemble((WhileStatement) statement));
        else if (statement instanceof SimpleFunctionCall)
            stackBlock.push(FunctionCallAssembler.assemble((SimpleFunctionCall) statement));
        else if (statement instanceof SimpleReturn) {
            stackBlock.push(new StopThisScriptBlock());
        } else if (statement instanceof SystemCallStatement) {
            stackBlock.push(SystemCallAssembler.assemble((SystemCallStatement) statement));
        } else if (statement instanceof SimpleArrayDeclaration) {
            stackBlock.push(ArrayDeclarationAssembler.assemble((SimpleArrayDeclaration) statement));
        } else if (statement instanceof SimpleVariableDeclaration) {
        } else
            stackBlock.push(errorBlock(statement));

        return stackBlock;
    }

    static StackBlock errorBlock(Statement statement) {
        return new SayBlock("Compiler Error: " + statement.toString());
    }
}
