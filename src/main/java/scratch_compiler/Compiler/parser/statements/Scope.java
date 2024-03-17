package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

public class Scope extends Statement {
    private ArrayList<Statement> statements;

    public Scope() {
        statements = new ArrayList<>();
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public ArrayList<Statement> getStatements() {
        return new ArrayList<>(statements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\n");
        for (Statement statement : statements)
            sb.append("   ").append(statement).append("\n");

        sb.append("}");
        return sb.toString();
    }

    public ArrayList<Statement> getChildren() {
        return new ArrayList<>(statements);
    }
}
