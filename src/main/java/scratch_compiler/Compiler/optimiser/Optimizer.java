package scratch_compiler.Compiler.optimiser;

import java.util.ArrayList;
import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.optimiser.constant_folding.ConstantFolding;
import scratch_compiler.Compiler.optimiser.unreachable_code.UnreachableCode;

public class Optimizer {
    private static ArrayList<Optimization> optimizations = new ArrayList<Optimization>() {
        {
            add(new UnreachableCode());
            add(new ConstantFolding());
        }
    };

    public static CompiledCode optimize(CompiledCode compiledCode) {
        System.out.println("Optimising...");
        System.out.println(compiledCode);

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Optimization optimization : optimizations) {
                Optimized optimized = optimization.optimize(compiledCode);
                if (optimized.isOptimized()) {
                    compiledCode = (CompiledCode) optimized.getObject();
                    System.out.println("Optimised " + optimization.getClass().getSimpleName() + ":");
                    System.out.println(compiledCode);
                    changed = true;
                }
            }
        }
        return compiledCode;
    }

    // private static Scope optimise(Scope scope) {
    // Scope optimisedScope = new Scope(scope.declarationTable);
    // for (Statement statement : scope.getStatements()) {
    // if (statement instanceof Scope)
    // optimisedScope.addStatement(optimise((Scope) statement));
    // else if(statement instanceof ControlFlowStatement)
    // {
    // ControlFlowStatement controlFlowStatement = (ControlFlowStatement) statement;
    // controlFlowStatement.setExpression(ExpressionOptimiser.optimise(controlFlowStatement.getExpression()));
    // optimisedScope.addStatement(controlFlowStatement);
    // }
    // else if(statement instanceof VariableDeclaration)
    // {
    // VariableDeclaration variableDeclaration = (VariableDeclaration) statement;
    // variableDeclaration.setExpression(ExpressionOptimiser.optimise(variableDeclaration.getExpression()));
    // optimisedScope.addStatement(variableDeclaration);
    // }
    // else if(statement instanceof Assignment)
    // {
    // Assignment assignment = (Assignment) statement;
    // assignment.setExpression(ExpressionOptimiser.optimise(assignment.getExpression()));
    // optimisedScope.addStatement(assignment);
    // }
    // else
    // optimisedScope.addStatement(statement);

    // }

    // return scope;
    // }
}
