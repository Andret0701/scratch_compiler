package scratch_compiler.Compiler.ScratchAssembler;

import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ScopeAssembler {
    public static BlockStack assemble(Scope scope) {
        BlockStack stackBlock = new BlockStack();
        for (Statement statement : scope.getStatements()) {
            stackBlock.push(StatementAssembler.assemble(statement));
        }
        return stackBlock;
    }
}
