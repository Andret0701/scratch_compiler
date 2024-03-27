package scratch_compiler.Compiler;

import scratch_compiler.Compiler.parser.VariableType;

public class Type {
    private boolean isArray;
    private TypeDefinition type;

    public Type(TypeDefinition type, boolean isArray) {
        this.type = type;
        this.isArray = isArray;
    }

    public Type(Type type, boolean isArray) {
        this(type.getType(), isArray);
    }

    public Type(TypeDefinition type) {
        this(type, false);
    }

    public Type(VariableType type) {
        this(new TypeDefinition(type), false);
    }

    public TypeDefinition getType() {
        return type;
    }

    public boolean isArray() {
        return isArray;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Type) {
            Type other = (Type) obj;
            return type.equals(other.type) && isArray == other.isArray;
        }
        return false;
    }

    @Override
    public String toString() {
        return type.toString() + (isArray ? "[]" : "");
    }
}
