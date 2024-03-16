package scratch_compiler.Compiler.optimiser;

import java.util.ArrayList;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.ControlFlowStatement;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class Optimiser {
    
    public static CompiledCode optimise(CompiledCode compiledCode) {
        ArrayList<FunctionDeclaration> functions = new ArrayList<>();
        for (FunctionDeclaration function : compiledCode.getFunctions()) {
            functions.add(new FunctionDeclaration(new Function(function.getFunction().getName(), function.getFunction().getReturnType(), function.getFunction().getArguments()), optimise(function.getScope())));
        }

        return new CompiledCode(functions);
    }

    private static Scope optimise(Scope scope) {
        Scope optimisedScope = new Scope(scope.declarationTable);
        for (Statement statement : scope.getStatements()) {
            if (statement instanceof Scope)
                optimisedScope.addStatement(optimise((Scope) statement));
            else if(statement instanceof ControlFlowStatement)
            {
                ControlFlowStatement controlFlowStatement = (ControlFlowStatement) statement;
                controlFlowStatement.setExpression(ExpressionOptimiser.optimise(controlFlowStatement.getExpression()));
                optimisedScope.addStatement(controlFlowStatement);
            }
            else if(statement instanceof VariableDeclaration)
            {
                VariableDeclaration variableDeclaration = (VariableDeclaration) statement;
                variableDeclaration.setExpression(ExpressionOptimiser.optimise(variableDeclaration.getExpression()));
                optimisedScope.addStatement(variableDeclaration);
            }
            else if(statement instanceof Assignment)
            {
                Assignment assignment = (Assignment) statement;
                assignment.setExpression(ExpressionOptimiser.optimise(assignment.getExpression()));
                optimisedScope.addStatement(assignment);
            }
            else
                optimisedScope.addStatement(statement);
            
        }

        return scope;
    }
}
