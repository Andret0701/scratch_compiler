package scratch_compiler.Compiler.optimiser.constant_folding;

import java.util.HashMap;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class CopyConstants implements Optimization {
    @Override
    public Optimized optimize(CompiledCode code) {
        return new Optimized(code, false);
    }

    private Optimized copyConstants(Scope scope) {
        HashMap<String, Expression> constants = new HashMap<String, Expression>();
        for (int i = 0; i < scope.getStatements().size(); i++) {
            Statement statement = scope.getStatements().get(i);
            if (statement instanceof VariableDeclaration) {
                VariableDeclaration declaration = (VariableDeclaration) statement;
                if (declaration.getExpression().isConstant())
                    constants.put(declaration.getName(), declaration.getExpression());                
            }

            for 
        }
        return new Optimized(scope, false);
    }
}