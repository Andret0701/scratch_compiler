package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.intermediate.simple_code.SimpleReturn;
import scratch_compiler.Compiler.parser.statements.ReturnStatement;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertReturn {
    public static ArrayList<Statement> convert(ReturnStatement returnStatement, IntermediateTable table) {
        ArrayList<Statement> statements = new ArrayList<>();
        statements.addAll(ConvertStack.push(returnStatement.getExpression(), table));
        statements.add(new SimpleReturn());
        return statements;
    }
}
