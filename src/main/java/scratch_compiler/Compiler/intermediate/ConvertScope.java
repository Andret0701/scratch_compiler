package scratch_compiler.Compiler.intermediate;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.ScopeContainer;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class ConvertScope {
    public static void convert(ScopeContainer container) {
        if (container == null)
            return;

        for (int i = 0; i < container.getScopeCount(); i++) {
            Scope converted = convertScope(container.getScope(i));
            convert(converted);
            container.setScope(i, converted);
        }
    }

    public static Scope convertScope(Scope scope) {
        Scope convertedScope = new Scope();
        for (Statement statement : scope.getStatements()) {
            ArrayList<Statement> convertedStatements = ConvertStatement.convert(statement);
            for (Statement convertedStatement : convertedStatements) {
                convertedScope.addStatement(convertedStatement);
            }
        }
        convert(convertedScope);
        return convertedScope;
    }
}
