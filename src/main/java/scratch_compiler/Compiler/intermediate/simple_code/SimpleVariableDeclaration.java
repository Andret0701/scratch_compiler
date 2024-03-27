package scratch_compiler.Compiler.intermediate.simple_code;

import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.statements.Statement;

public class SimpleVariableDeclaration extends Statement {

    private String name;
    private VariableType type;

    public SimpleVariableDeclaration(String name, VariableType type) {
        super(0);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
