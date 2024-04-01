package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

public class Scope extends Statement {
    private ArrayList<Statement> statements;

    public Scope() {
        statements = new ArrayList<>();
    }

    public Scope(Statement statement) {
        this();
        if (statement instanceof Scope) {
            for (Statement s : ((Scope) statement).getStatements()) {
                addStatement(s);
            }
        } else
            addStatement(statement);
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public void addAllStatements(ArrayList<Statement> statements) {
        this.statements.addAll(statements);
    }

    public ArrayList<Statement> getStatements() {
        return new ArrayList<>(statements);
    }

    @Override
    public String toString() {
        String out = "";
        if (statements.size() > 1)
            out += "{";
        out += "\n";
        for (Statement statement : statements)
            out += "   " + statement.toString().replace("\n", "\n   ") + "\n";

        if (statements.size() > 1)
            out += "}";
        else
            out = out.substring(0, out.length() - 1);
        return out;
    }

    @Override
    public Scope getScope(int index) {
        int scopeCounter = 0;
        for (Statement statement : statements) {
            if (statement instanceof Scope) {
                if (scopeCounter == index)
                    return (Scope) statement;
                scopeCounter++;
            }
        }
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

    @Override
    public int getScopeCount() {
        int count = 0;
        for (Statement statement : statements)
            if (statement instanceof Scope)
                count++;
        return count;
    }

    @Override
    public void setScope(int index, Scope scope) {
        int scopeCounter = 0;
        for (int i = 0; i < statements.size(); i++) {
            if (statements.get(i) instanceof Scope) {
                if (scopeCounter == index) {
                    statements.set(i, scope);
                    return;
                }
                scopeCounter++;
            }
        }
        throw new IndexOutOfBoundsException(index + " is out of bounds for this container");
    }

}
