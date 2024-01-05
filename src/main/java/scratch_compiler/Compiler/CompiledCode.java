package scratch_compiler.Compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.lexer.Lexer;
import scratch_compiler.Compiler.lexer.Token;
import scratch_compiler.Compiler.lexer.TokenReader;
import scratch_compiler.Compiler.parser.StatementParser;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class CompiledCode {
    private Scope scope;
    public CompiledCode(String code) {
        this(code, new IdentifierTypes());
    }
    public CompiledCode(String code, IdentifierTypes identifierTypes) {
        ArrayList<Token> tokens = Lexer.lex(code);
        System.out.println(tokens);

        TokenReader reader = new TokenReader(tokens);

        Scope scope = new Scope(identifierTypes);
        Statement statement;
        while ((statement = StatementParser.parse(reader, scope.getIdentifierTypes())) != null) 
            scope.addStatement(statement);

        this.scope = scope;   
    }

    public Scope getScope() {
        return scope;
    }
}
