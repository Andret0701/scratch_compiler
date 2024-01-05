package scratch_compiler.Compiler;

import java.util.HashMap;
import scratch_compiler.Compiler.parser.VariableType;

public class IdentifierTypes {
    private HashMap<String, VariableType> types;

    public IdentifierTypes() {
        types = new HashMap<>();
    }

    public void add(String name, VariableType type) {
        types.put(name, type);
    }

    public VariableType get(String name) {
        if (!contains(name))
            throw new IllegalArgumentException("Variable " + name + " is not declared");
        return types.get(name);
    }

    public boolean contains(String name) {
        return types.containsKey(name);
    }

    public void validateDeclaration(String name, int line) {
        if (contains(name))
            throw new IllegalArgumentException("Redeclaration of variable " + name + " at line " + line);
    }

    public void validateUsage(String name, int line) {
        if (!contains(name))
            throw new IllegalArgumentException("Variable " + name + " is not declared at line " + line);
    }

    public IdentifierTypes copy() {
        IdentifierTypes copy = new IdentifierTypes();
        copy.types = new HashMap<>(types);
        return copy;
    }
}
