package scratch_compiler.Compiler;

import java.util.HashMap;

import scratch_compiler.Compiler.lexer.TokenType;

public class IdentifierTypes {
    private HashMap<String, TokenType> types;

    public IdentifierTypes() {
        types = new HashMap<>();
    }

    public void add(String name, TokenType type) {
        types.put(name, type);
    }

    public TokenType get(String name) {
        if (!contains(name))
            return null;
        return types.get(name);
    }

    public boolean contains(String name) {
        return types.containsKey(name);
    }

    public IdentifierTypes copy() {
        IdentifierTypes copy = new IdentifierTypes();
        copy.types = (HashMap<String, TokenType>) types.clone();
        return copy;
    }
}
