package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

public abstract class Statement {
    public Statement() {
    }

    public abstract ArrayList<Statement> getChildren();
}
