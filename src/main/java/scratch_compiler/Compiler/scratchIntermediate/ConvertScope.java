package scratch_compiler.Compiler.scratchIntermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertScope {
    public static Scope convert(Scope scope) {
        Scope converted = new Scope();
        for (Statement statement : scope.getStatements()) {
            ArrayList<Statement> statements = ConvertStatement.convert(statement);
            converted.addAllStatements(statements);
        }

        return converted;
    }
}
