package scratch_compiler.Compiler.optimiser.stack_optimizer;

import scratch_compiler.Compiler.SystemCall;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.ArrayPop;
import scratch_compiler.Compiler.intermediate.simple_code.Pop;
import scratch_compiler.Compiler.intermediate.simple_code.Push;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.SystemCallStatement;

public class StackOptimizer implements Optimization {
    @Override
    public Optimized optimize(IntermediateCode code) {
        // System.out.println(code);
        boolean changed = false;

        Optimized globalOptimized = optimizePushPop(code.getGlobalScope());
        changed = changed || globalOptimized.isOptimized();
        code.setGlobalScope((Scope) globalOptimized.getObject());

        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            Optimized functionOptimized = optimizePushPop(function.getScope());
            changed = changed || functionOptimized.isOptimized();
            function.setScope((Scope) functionOptimized.getObject());
        }

        return new Optimized(code, changed);
    }

    private static Optimized optimizePushPop(Scope scope) {
        boolean changed = false;
        Scope optimizedScope = new Scope();
        for (int i = 0; i < scope.getStatements().size(); i++) {
            Statement statement = scope.getStatements().get(i);
            if (statement instanceof Push && i < scope.getStatements().size() - 1) {
                Push push = (Push) statement;
                // System.out.println(push + " is looking for a pop");
                Statement nextStatement = scope.getStatements().get(i + 1);
                // System.out.println(" Next statement: " + nextStatement);
                while (nextStatement instanceof SimpleVariableDeclaration
                        || nextStatement instanceof SimpleArrayDeclaration
                        || nextStatement instanceof SystemCallStatement
                        || validAssignment(nextStatement, push.getExpression())) {
                    optimizedScope.addStatement(nextStatement);
                    i++;
                    if (i < scope.getStatements().size() - 1) {
                        nextStatement = scope.getStatements().get(i + 1);
                        if (nextStatement instanceof Push)
                            break;
                        // System.out.println(" Next statement: " + nextStatement);

                    } else {
                        nextStatement = null;
                        break;
                    }
                }

                if (nextStatement instanceof Push) {
                    optimizedScope.addStatement(push);
                    continue;
                }

                if (nextStatement instanceof Pop) {
                    Pop pop = (Pop) nextStatement;

                    changed = true;
                    optimizedScope.addStatement(new SimpleVariableAssignment(pop.getName(),
                            push.getExpression()));
                    i++;
                    continue;
                } else if (nextStatement instanceof ArrayPop) {
                    ArrayPop arrayPop = (ArrayPop) nextStatement;

                    changed = true;
                    optimizedScope.addStatement(
                            new SimpleArrayAssignment(arrayPop.getName(), arrayPop.getIndex(),
                                    push.getExpression()));
                    i++;
                    continue;
                }

                optimizedScope.addStatement(statement);
                if (nextStatement == null)
                    continue;

                statement = nextStatement;
                i++;
            }

            if (statement instanceof Scope) {
                Optimized optimized = optimizePushPop((Scope) statement);
                changed = changed || optimized.isOptimized();
                optimizedScope.addStatement((Scope) optimized.getObject());
                continue;
            }

            optimizedScope.addStatement(statement);
            for (int j = 0; j < statement.getScopeCount(); j++) {
                Optimized optimized = optimizePushPop(statement.getScope(j));
                changed = changed || optimized.isOptimized();
                statement.setScope(j, (Scope) optimized.getObject());
            }

        }

        return new Optimized(optimizedScope, changed);
    }

    private static boolean validAssignment(Statement statement, Expression expression) {
        if (statement instanceof SimpleVariableAssignment) {
            SimpleVariableAssignment variableAssignment = (SimpleVariableAssignment) statement;
            return !conainsVariable(expression, variableAssignment.getName());
        }

        return false;
    }

    private static boolean conainsVariable(Expression expression, String variable) {
        if (expression instanceof SimpleVariableValue) {
            SimpleVariableValue variableValue = (SimpleVariableValue) expression;
            return variableValue.getName().equals(variable);
        }

        if (expression instanceof SimpleArrayValue) {
            SimpleArrayValue arrayValue = (SimpleArrayValue) expression;
            return conainsVariable(arrayValue.getIndex(), variable) || arrayValue.getName().equals(variable);

        }

        for (int i = 0; i < expression.getExpressionCount(); i++) {
            if (conainsVariable(expression.getExpression(i), variable))
                return true;
        }

        return false;
    }

}
