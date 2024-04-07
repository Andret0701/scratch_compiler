package scratch_compiler.Compiler.intermediate.validator;

import java.util.ArrayList;

public class SimpleDeclarationTable {
    private ArrayList<String> variables = new ArrayList<>();
    private ArrayList<String> arrays = new ArrayList<>();

    public void addVariable(String name) {
        variables.add(name);
    }

    public void addArray(String name) {
        arrays.add(name);
    }

    public boolean containsVariable(String name) {
        return variables.contains(name);
    }

    public boolean containsArray(String name) {
        return arrays.contains(name);
    }

    private void validateDeclaration(String name) {
        if (containsVariable(name) || containsArray(name))
            throw new RuntimeException("Variable " + name + " already declared");
    }

    public void declareVariable(String name) {
        validateDeclaration(name);
        addVariable(name);
    }

    public void declareArray(String name) {
        validateDeclaration(name);
        addArray(name);
    }

    public void validateVariableUsage(String name) {
        if (!containsVariable(name))
            throw new RuntimeException("Variable " + name + " not declared");
    }

    public void validateArrayUsage(String name) {
        if (!containsArray(name))
            throw new RuntimeException("Array " + name + " not declared");
    }

    public SimpleDeclarationTable copy() {
        SimpleDeclarationTable copy = new SimpleDeclarationTable();
        copy.variables = new ArrayList<>(variables);
        copy.arrays = new ArrayList<>(arrays);
        return copy;
    }
}
