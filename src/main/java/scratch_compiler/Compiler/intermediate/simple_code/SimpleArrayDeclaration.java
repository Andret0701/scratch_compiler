package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.statements.Statement;

public class SimpleArrayDeclaration extends Statement {
    private String name;
    private VariableType type;

    public SimpleArrayDeclaration(String name, VariableType type, Expression size) {
        super(1);
        this.name = name;
        this.type = type;
        setExpression(0, size);
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }

    public Expression getSize() {
        return getExpression(0);
    }

    @Override
    public String toString() {
        return type + " " + name + " = " + type + "[" + getSize() + "]";
    }
}