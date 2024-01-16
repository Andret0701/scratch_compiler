package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.parser.expressions.Expression;

public class ForStatement extends ControlFlowStatement {
    private VariableDeclaration declaration;
    private Assignment increment;
    public ForStatement(VariableDeclaration declaration, Expression loopCondition, Assignment increment, Statement statement) {
        super(loopCondition, statement);
        this.declaration = declaration;
        this.increment = increment;
    }

    public VariableDeclaration getDeclaration() {
        return declaration;
    }

    public Assignment getIncrement() {
        return increment;
    }

    @Override
    public String toString() {
        String result = "for (" + declaration.toString() + "; " + getExpression().toString() + "; " + increment.toString() + ") ";
        result += getExpression().toString();
        return result;
    }

}