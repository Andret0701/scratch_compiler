package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;

public class Assignment extends Statement {
    private VariableValue variable;
    private Expression expression;

    public Assignment(VariableValue variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public VariableValue getVariable() {
        return variable;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return variable + " = " + getExpression();
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public ArrayList<Statement> getChildren() {
        return new ArrayList<Statement>();
    }

}
