package scratch_compiler.Compiler.optimiser.function_inlining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.ArrayPop;
import scratch_compiler.Compiler.intermediate.simple_code.Pop;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleArrayValue;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionCall;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleReturn;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableAssignment;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableDeclaration;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleVariableValue;
import scratch_compiler.Compiler.optimiser.CopyCode;
import scratch_compiler.Compiler.optimiser.Optimization;
import scratch_compiler.Compiler.optimiser.Optimized;
import scratch_compiler.Compiler.parser.ExpressionContainer;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class FunctionInlining implements Optimization {
    @Override
    public Optimized optimize(IntermediateCode code) {

        Set<SimpleFunctionDeclaration> functionsToInline = getFunctionsToInline(code.getFunctions());
        boolean changed = false;
        for (SimpleFunctionDeclaration function : functionsToInline) {
            ArrayList<String> usedVariableNames = getUsedVariableNames(function.getScope());
            Optimized optimized = inlineScope(function.getScope(), functionsToInline, usedVariableNames);
            changed = changed || optimized.isOptimized();
            function.setScope((Scope) optimized.getObject());
        }

        return new Optimized(code, changed);
    }

    private static Optimized inlineScope(Scope scope, Set<SimpleFunctionDeclaration> functionsToInline,
            ArrayList<String> usedVariableNames) {
        Scope newScope = new Scope();
        boolean changed = false;
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleFunctionCall) {
                SimpleFunctionCall functionCall = (SimpleFunctionCall) statement;
                SimpleFunctionDeclaration function = getFunctionDeclaration(functionCall, functionsToInline);
                if (function != null) {
                    Optimized optimized = inlineScope(CopyCode.copy(function.getScope()), functionsToInline,
                            usedVariableNames);
                    changed = true;
                    newScope.addAllStatements(
                            getStatementsToInline(((Scope) optimized.getObject()), usedVariableNames));
                } else
                    newScope.addStatement(statement);
            } else if (statement instanceof Scope) {
                Optimized optimized = inlineScope((Scope) statement, functionsToInline, usedVariableNames);
                changed = changed || optimized.isOptimized();
                newScope.addStatement((Scope) optimized.getObject());
            } else {
                newScope.addStatement(statement);
                for (int i = 0; i < statement.getScopeCount(); i++) {
                    Optimized optimized = inlineScope(statement.getScope(i), functionsToInline, usedVariableNames);
                    changed = changed || optimized.isOptimized();
                    statement.setScope(i, (Scope) optimized.getObject());
                }
            }
        }
        return new Optimized(newScope, changed);
    }

    private Set<SimpleFunctionDeclaration> getFunctionsToInline(ArrayList<SimpleFunctionDeclaration> functions) {
        Set<SimpleFunctionDeclaration> functionsToInline = new HashSet<SimpleFunctionDeclaration>();
        for (SimpleFunctionDeclaration function : functions) {
            if (!isFunctionRecursive(function) && hasOneReturn(function)) {
                functionsToInline.add(function);
            }
        }

        return functionsToInline;
    }

    private static boolean hasOneReturn(SimpleFunctionDeclaration function) {
        return getReturnCount(function.getScope()) == 1 && function.getScope().getStatements()
                .get(function.getScope().getStatements().size() - 1) instanceof SimpleReturn;
    }

    private static int getReturnCount(Scope scope) {
        int returnCount = 0;
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleReturn) {
                returnCount++;
            } else if (statement instanceof Scope) {
                returnCount += getReturnCount((Scope) statement);
            } else {
                for (int i = 0; i < statement.getScopeCount(); i++) {
                    returnCount += getReturnCount(statement.getScope(i));
                }
            }
        }
        return returnCount;
    }

    private static boolean isFunctionRecursive(SimpleFunctionDeclaration function) {
        return isFunctionRecursive(function.getScope(), function.getName());
    }

    private static boolean isFunctionRecursive(Scope scope, String functionName) {
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleFunctionCall) {
                SimpleFunctionCall functionCall = (SimpleFunctionCall) statement;
                if (functionCall.getName().equals(functionName))
                    return true;
            }

            if (statement instanceof Scope) {
                if (isFunctionRecursive((Scope) statement, functionName))
                    return true;
            }

            for (int i = 0; i < statement.getScopeCount(); i++) {
                if (isFunctionRecursive(statement.getScope(i), functionName))
                    return true;
            }
        }
        return false;
    }

    private static SimpleFunctionDeclaration getFunctionDeclaration(SimpleFunctionCall functionCall,
            Set<SimpleFunctionDeclaration> functions) {
        for (SimpleFunctionDeclaration function : functions) {
            if (function.getName().equals(functionCall.getName()))
                return function;
        }
        return null;
    }

    private static ArrayList<Statement> getStatementsToInline(Scope scope, ArrayList<String> usedVariableNames) {
        ArrayList<Statement> statements = new ArrayList<>();
        scope = fixUsedVariableNames(scope, usedVariableNames);
        for (Statement statement : scope.getStatements()) {
            if (!(statement instanceof SimpleReturn))
                statements.add(statement);
        }

        usedVariableNames.addAll(getUsedVariableNames(scope));
        return statements;
    }

    private static ArrayList<String> getUsedVariableNames(Scope scope) {
        ArrayList<String> variableNames = new ArrayList<>();
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleVariableDeclaration)
                variableNames.add(((SimpleVariableDeclaration) statement).getName());
            else if (statement instanceof SimpleFunctionCall)
                variableNames.add(((SimpleFunctionCall) statement).getName());
            if (statement instanceof Scope) {
                variableNames.addAll(getUsedVariableNames((Scope) statement));
            } else {
                for (int i = 0; i < statement.getScopeCount(); i++) {
                    variableNames.addAll(getUsedVariableNames(statement.getScope(i)));
                }
            }
        }
        return variableNames;
    }

    private static Scope fixUsedVariableNames(Scope scope, ArrayList<String> usedNames) {
        Scope newScope = new Scope();
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof SimpleVariableDeclaration) {
                SimpleVariableDeclaration variableDeclaration = (SimpleVariableDeclaration) statement;
                String name = fixName(variableDeclaration.getName(), usedNames);
                newScope.addStatement(new SimpleVariableDeclaration(name, variableDeclaration.getType()));
            } else if (statement instanceof SimpleArrayDeclaration) {
                SimpleArrayDeclaration arrayDeclaration = (SimpleArrayDeclaration) statement;
                String name = fixName(arrayDeclaration.getName(), usedNames);
                newScope.addStatement(
                        new SimpleArrayDeclaration(name, arrayDeclaration.getType(), arrayDeclaration.getSize()));
            } else if (statement instanceof Pop) {
                Pop pop = (Pop) statement;
                String name = fixName(pop.getName(), usedNames);
                newScope.addStatement(new Pop(name));
            } else if (statement instanceof ArrayPop) {
                ArrayPop arrayPop = (ArrayPop) statement;
                String name = fixName(arrayPop.getName(), usedNames);
                newScope.addStatement(new ArrayPop(name, arrayPop.getIndex()));
            } else if (statement instanceof SimpleVariableAssignment) {
                SimpleVariableAssignment variableAssignment = (SimpleVariableAssignment) statement;
                String name = fixName(variableAssignment.getName(), usedNames);
                newScope.addStatement(new SimpleVariableAssignment(name, variableAssignment.getValue()));
            } else if (statement instanceof SimpleArrayAssignment) {
                SimpleArrayAssignment arrayAssignment = (SimpleArrayAssignment) statement;
                String name = fixName(arrayAssignment.getName(), usedNames);
                newScope.addStatement(
                        new SimpleArrayAssignment(name, arrayAssignment.getIndex(), arrayAssignment.getValue()));
            } else if (statement instanceof Scope) {
                newScope.addStatement(fixUsedVariableNames((Scope) statement, usedNames));
            } else {
                newScope.addStatement(statement);
                for (int i = 0; i < statement.getScopeCount(); i++) {
                    statement.setScope(i, fixUsedVariableNames(statement.getScope(i), usedNames));
                }
            }
            fixUsedVariableNames((ExpressionContainer) statement, usedNames);
        }
        return newScope;
    }

    private static void fixUsedVariableNames(ExpressionContainer container, ArrayList<String> usedNames) {
        for (int i = 0; i < container.getExpressionCount(); i++) {
            if (container.getExpression(i) instanceof SimpleVariableValue) {
                SimpleVariableValue variableValue = (SimpleVariableValue) container.getExpression(i);
                String name = fixName(variableValue.getName(), usedNames);
                container.setExpression(i, new SimpleVariableValue(name, variableValue.getType().getType().getType()));
            } else if (container.getExpression(i) instanceof SimpleArrayValue) {
                SimpleArrayValue arrayValue = (SimpleArrayValue) container.getExpression(i);
                String name = fixName(arrayValue.getName(), usedNames);
                container.setExpression(i, new SimpleArrayValue(name, arrayValue.getType().getType().getType(),
                        arrayValue.getIndex()));
            }
            fixUsedVariableNames(container.getExpression(i), usedNames);
        }
    }

    private static String fixName(String name, ArrayList<String> usedNames) {
        if (usedNames.contains(name)) {
            return fixName(name + "_", usedNames);
        }
        return name;
    }

}
