package scratch_compiler.Compiler.optimiser.constant_folding;

import java.util.HashMap;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;
import scratch_compiler.Compiler.parser.statements.WhileStatement;

public class CopyConstants implements Optimization {
    @Override
    public Optimized optimize(IntermediateCode code) {
        boolean changed = false;
        Optimized optimizedGlobalScope = copyConstants(code.getGlobalScope(), new HashMap<>(), true);
        changed = changed || optimizedGlobalScope.isOptimized();
        code.setGlobalScope((Scope) optimizedGlobalScope.getObject());

        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            Optimized optimizedFunctionScope = copyConstants(function.getScope(), new HashMap<>(), true);
            changed = changed || optimizedFunctionScope.isOptimized();
            function.setScope((Scope) optimizedFunctionScope.getObject());
        }

        return new Optimized(code, changed);
    }

    private Optimized copyConstants(Scope scope, HashMap<String, Expression> constants, boolean add) {
        boolean changed = false;
        for (int i = 0; i < scope.getStatements().size(); i++) {
            Statement statement = scope.getStatements().get(i);

            if (!(statement instanceof WhileStatement) && add) {
                Optimized optimized = copyConstants(statement, constants);
                changed = changed || optimized.isOptimized();
            }

            if (statement instanceof SimpleVariableAssignment) {
                SimpleVariableAssignment assignment = (SimpleVariableAssignment) statement;
                if (assignment.getValue().isConstant() & add) {
                    constants.put(assignment.getName(), assignment.getValue());
                } else
                    constants.remove(assignment.getName());
            }

            if (statement instanceof SimpleFunctionCall)
                constants.clear(); // fix this later

            boolean remove = (statement instanceof IfStatement) || (statement instanceof WhileStatement);
            for (int j = 0; j < statement.getScopeCount(); j++) {
                Optimized innerOptimized = copyConstants(statement.getScope(j), constants, !remove);
                changed = changed || innerOptimized.isOptimized();
                statement.setScope(j, (Scope) innerOptimized.getObject());
            }
        }
        return new Optimized(scope, changed);
    }

    private Optimized copyConstants(ExpressionContainer container, HashMap<String, Expression> constants) {
        boolean changed = false;
        for (int i = 0; i < container.getExpressionCount(); i++) {
            Expression expression = container.getExpression(i);
            Optimized optimized = copyConstants(expression, constants);
            changed = changed || optimized.isOptimized();

            if (expression instanceof SimpleVariableValue) {
                SimpleVariableValue variableValue = (SimpleVariableValue) expression;
                if (constants.containsKey(variableValue.getName())) {
                    container.setExpression(i, constants.get(variableValue.getName()));
                    changed = true;
                }
            }
        }
        return new Optimized(container, changed);
    }
}