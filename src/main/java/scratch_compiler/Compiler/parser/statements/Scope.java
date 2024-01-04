package scratch_compiler.Compiler.parser.statements;

import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.lexer.TokenType;
import scratch_compiler.Compiler.parser.nodes.Expression;

import java.util.ArrayList;



public class Scope extends Statement {
    private IdentifierTypes identifierTypes;
    private ArrayList<Statement> statements;

    public Scope(IdentifierTypes identifierTypes) {
        super(TokenType.OPEN_BRACE, null);
        statements = new ArrayList<>();
        this.identifierTypes = identifierTypes;
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public void addIdentifierType(String name, TokenType type) {
        identifierTypes.add(name, type);
    }

    public IdentifierTypes getIdentifierTypes() {
        return identifierTypes;
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

}
