package scratch_compiler.Compiler.optimiser.unreachable_code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleReturn;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.statements.ErrorStatement;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.WhileStatement;

public class UnreachableCode implements Optimization {
    @SuppressWarnings("unchecked")
    @Override
    public Optimized optimize(IntermediateCode code) {
        boolean changed = false;

        Optimized globalOptimized = optimize(code.getGlobalScope());
        changed = changed || globalOptimized.isOptimized();
        code.setGlobalScope((Scope) globalOptimized.getObject());

        for (SimpleFunctionDeclaration function : code.getFunctions()) {
            Optimized functionOptimized = optimize(function.getScope());
            changed = changed || functionOptimized.isOptimized();
            function.setScope((Scope) functionOptimized.getObject());
        }

        Optimized referencedFunctions = getReferencedFunctions(code.getFunctions(),
                code.getGlobalScope());
        changed = changed || referencedFunctions.isOptimized();
        code.setFunctions((ArrayList<SimpleFunctionDeclaration>) referencedFunctions.getObject());

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
                    optimisedScope.addAllStatements(ifStatement.getIfScope().getStatements());
                } else if (ifStatement.getExpression().equals(new BooleanValue(false))) {
                    changed = true;
                    if (ifStatement.getElseScope() != null)
                        optimisedScope.addAllStatements(ifStatement.getElseScope().getStatements());
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

        optimisedScope = fixMultipleReturns(optimisedScope);
        return new Optimized(optimisedScope, changed);
    }

    private Scope fixMultipleReturns(Scope scope) {
        Scope fixedScope = new Scope();
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleReturn || statement instanceof ErrorStatement) {
                fixedScope.addStatement(statement);
                break;
            }

            for (int i = 0; i < statement.getScopeCount(); i++) {
                Scope child = statement.getScope(i);
                statement.setScope(i, fixMultipleReturns(child));
            }

            fixedScope.addStatement(statement);
        }

        return fixedScope;
    }

    private Optimized getReferencedFunctions(ArrayList<SimpleFunctionDeclaration> functions,
            Scope globalScope) {
        Set<SimpleFunctionDeclaration> referencedFunctions = new HashSet<>();
        for (Statement statement : globalScope.getStatements()) {
            referencedFunctions.addAll(getReferencedFunctions(statement, functions, referencedFunctions));
        }

        ArrayList<SimpleFunctionDeclaration> optimizedFunctions = new ArrayList<>(referencedFunctions);
        return new Optimized(optimizedFunctions, optimizedFunctions.size() != functions.size());
    }

    private Set<SimpleFunctionDeclaration> getReferencedFunctions(Statement statement,
            ArrayList<SimpleFunctionDeclaration> functions,
            Set<SimpleFunctionDeclaration> referencedFunctions) {

        if (statement instanceof SimpleFunctionCall) {
            SimpleFunctionCall functionCall = (SimpleFunctionCall) statement;
            SimpleFunctionDeclaration functionDeclaration = getFunctionDeclaration(functionCall.getName(),
                    functions);

            if (functionDeclaration != null && !referencedFunctions.contains(functionDeclaration)) {
                referencedFunctions.add(functionDeclaration);
                referencedFunctions
                        .addAll(getReferencedFunctions(functionDeclaration.getScope(), functions, referencedFunctions));
            }
        }

        if (statement instanceof Scope) {
            Scope scope = (Scope) statement;
            for (Statement scopeStatement : scope.getStatements()) {
                referencedFunctions.addAll(getReferencedFunctions(scopeStatement, functions, referencedFunctions));
            }
        } else {
            for (int i = 0; i < statement.getScopeCount(); i++) {
                Scope child = statement.getScope(i);
                for (Statement scopeStatement : child.getStatements()) {
                    referencedFunctions.addAll(getReferencedFunctions(scopeStatement, functions, referencedFunctions));
                }
            }
        }

        return referencedFunctions;
    }

    private SimpleFunctionDeclaration getFunctionDeclaration(String function,
            ArrayList<SimpleFunctionDeclaration> functions) {
        for (SimpleFunctionDeclaration functionDeclaration : functions) {
            if (functionDeclaration.getName().equals(function))
                return functionDeclaration;
        }

        return null;
    }
}
