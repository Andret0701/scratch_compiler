package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class VariableDeclaration extends Statement {
    private Variable variable;

    public VariableDeclaration(String name, Type type, Expression value) {
        super(1);
        this.variable = new Variable(name, type);
        validateValue(value);
        setExpression(0, value);
    }

    private void validateValue(Expression value) {
        // add some array check stuff
        System.out.println(variable + ": " + value);
        if (variable.getType().isArray() && value == null)
            throw new RuntimeException("Array declaration must have a value");

        if (value == null)
            return;

        if (!value.getType().equals(variable.getType()))
            throw new RuntimeException("Invalid type for variable " + variable.getName());
    }

    public Variable getVariable() {
        return variable;
    }

    public Expression getExpression() {
        return getExpression(0);
    }

    @Override
    public String toString() {
        if (getExpression() == null)
            return variable.toString();

        return variable + " = " + getExpression();
    }

    public ArrayList<Statement> getStatements() {
        return new ArrayList<Statement>();
    }
}