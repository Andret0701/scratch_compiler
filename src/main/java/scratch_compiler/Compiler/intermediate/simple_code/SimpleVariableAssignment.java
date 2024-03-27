package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Statement;

public class SimpleVariableAssignment extends Statement {
    private String name;

    public SimpleVariableAssignment(String name, Expression value) {
        super(1);
        this.name = name;
        setExpression(0, value);
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return getExpression(0);
    }

    @Override
    public String toString() {
        return name + " = " + getValue();
    }
}
