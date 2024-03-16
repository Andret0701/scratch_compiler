package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class VariableDeclaration extends Statement {
    private String name;
    private Type type;
    private Expression value;

    public VariableDeclaration(String name, Type type, Expression value) {
        this.name = name;
        this.type = type;
        validateValue(value);
        this.value = value;
    }

    private void validateValue(Expression value) {
        if (value == null)
            throw new RuntimeException("Invalid value for variable " + name);
        if (!value.getType().equals(type))
            throw new RuntimeException("Invalid type for variable " + name);
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name + " = " + value;
    }

    public ArrayList<Statement> getChildren() {
        return new ArrayList<Statement>();
    }
}