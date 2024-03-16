package scratch_compiler.Compiler;

import scratch_compiler.Compiler.parser.VariableReference;
import scratch_compiler.Compiler.parser.VariableType;

public class Variable {
    private String name;
    private Type type;
    private boolean isArray;
    private int arraySize;

    // find a way to store variable references like this:
    // vector[4].x[2].y[19];
    // vector[4].x[2].y[19] = 5;
    // vector

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
        this.isArray = false;
        this.arraySize = 0;
    }

    public Variable(String name, Type type, int arraySize) {
        this.name = name;
        this.type = type;
        this.isArray = true;
        this.arraySize = arraySize;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isArray() {
        return isArray;
    }

    public int getArraySize() {
        return arraySize;
    }

    public Variable reference(VariableReference reference) {
        if (reference == null)
            return this;

        if (type.getType() != VariableType.STRUCT)
            throw new RuntimeException("Cannot reference field of non-struct type");

        Variable field = type.getField(reference.getName());
        if (field == null)
            throw new RuntimeException("Field " + reference.getName() + " not found in struct " + type.getName());

        return field.reference(reference.getNext());
    }

    public boolean containsReference(VariableReference reference) {
        if (reference == null)
            return true;

        if (type.getType() != VariableType.STRUCT)
            return false;

        Variable field = type.getField(reference.getName());
        if (field == null)
            return false;

        return field.containsReference(reference.getNext());
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

}
