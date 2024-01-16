package scratch_compiler.Compiler.optimiser;

import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.parser.expressions.OperationExpression;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.ControlFlowStatement;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;

public class Optimiser {
    
    public static CompiledCode optimise(CompiledCode compiledCode) {
        return new CompiledCode(optimise(compiledCode.getScope()));
    }

    private static Scope optimise(Scope scope) {
        Scope optimisedScope = new Scope(scope.getIdentifierTypes());
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
