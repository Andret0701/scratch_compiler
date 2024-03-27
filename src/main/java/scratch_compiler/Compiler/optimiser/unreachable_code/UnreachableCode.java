package scratch_compiler.Compiler.optimiser.unreachable_code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.WhileStatement;

public class UnreachableCode implements Optimization {
    @SuppressWarnings("unchecked")
    @Override
    public Optimized optimize(CompiledCode code) {
        boolean changed = false;

        Optimized globalOptimized = optimize(code.getGlobalScope());
        changed = changed || globalOptimized.isOptimized();
        code.setGlobalScope((Scope) globalOptimized.getObject());

        for (FunctionDeclaration function : code.getFunctions()) {
            Optimized functionOptimized = optimize(function.getScope());
            changed = changed || functionOptimized.isOptimized();
            function.setScope((Scope) functionOptimized.getObject());
        }

        Optimized referencedFunctions = getReferencedFunctions(code.getFunctions(), code.getGlobalScope());
        changed = changed || referencedFunctions.isOptimized();
        code.setFunctions((ArrayList<FunctionDeclaration>) referencedFunctions.getObject());

        return new Optimized(code, changed);
    }

    private Optimized optimize(Scope scope) {
        boolean changed = false;
        Scope optimisedScope = new Scope();
        for (int i = 0; i < scope.getStatements().size(); i++) {
            Statement statement = scope.getStatements().get(i);
            if (statement instanceof Scope) {
                Optimized optimised = optimize((Scope) statement);
                changed = changed || optimised.isOptimized();
                optimisedScope.addStatement((Scope) optimised.getObject());
            } else if (statement instanceof IfStatement) {
                IfStatement ifStatement = (IfStatement) statement;
                if (ifStatement.getExpression().equals(new BooleanValue(true))) {
                    changed = true;
                    optimisedScope.addStatement(ifStatement.getStatement());
                } else if (ifStatement.getExpression().equals(new BooleanValue(false))) {
                    changed = true;
                    if (ifStatement.getElseStatement() != null)
                        optimisedScope.addStatement(ifStatement.getElseStatement());
                } else
                    optimisedScope.addStatement(ifStatement);

            } else if (statement instanceof WhileStatement) {
                WhileStatement whileStatement = (WhileStatement) statement;
                if (whileStatement.getExpression().equals(new BooleanValue(false))) {
                    changed = true;
                } else
                    optimisedScope.addStatement(whileStatement);
            } else
                optimisedScope.addStatement(statement);
        }

        return new Optimized(optimisedScope, changed);
    }

    private Optimized getReferencedFunctions(ArrayList<FunctionDeclaration> functions,
            Scope globalScope) {
        Set<FunctionDeclaration> referencedFunctions = getReferencedFunctions(globalScope, functions,
                new HashSet<FunctionDeclaration>());

        ArrayList<FunctionDeclaration> optimizedFunctions = new ArrayList<>(referencedFunctions);
        return new Optimized(optimizedFunctions, optimizedFunctions.size() != functions.size());
    }

    private Set<FunctionDeclaration> getReferencedFunctions(Statement statement,
            ArrayList<FunctionDeclaration> functions,
            Set<FunctionDeclaration> referencedFunctions) {
        if (statement instanceof FunctionCall) {
            FunctionCall functionCall = (FunctionCall) statement;
            FunctionDeclaration functionDeclaration = getFunctionDeclaration(functionCall.getFunction(), functions);
            if (functionDeclaration != null && !referencedFunctions.contains(functionDeclaration)) {
                referencedFunctions.add(functionDeclaration);
                referencedFunctions
                        .addAll(getReferencedFunctions(functionDeclaration.getScope(), functions, referencedFunctions));
            }
        }

        for (Statement child : statement.getStatements()) {
            referencedFunctions = getReferencedFunctions(child, functions, referencedFunctions);
        }

        return referencedFunctions;
    }

    private FunctionDeclaration getFunctionDeclaration(Function function, ArrayList<FunctionDeclaration> functions) {
        for (FunctionDeclaration functionDeclaration : functions) {
            if (functionDeclaration.getFunction().equals(function))
                return functionDeclaration;
        }

        return null;
    }
}
