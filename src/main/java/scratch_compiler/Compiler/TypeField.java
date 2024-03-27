package scratch_compiler.Compiler;

public class TypeField {
    private String name;
    private TypeDefinition type;

    public TypeField(String name, TypeDefinition type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public TypeDefinition getType() {
        return type;
    }
}
