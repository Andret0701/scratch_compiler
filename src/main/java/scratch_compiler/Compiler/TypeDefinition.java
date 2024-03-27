package scratch_compiler.Compiler;

import java.util.ArrayList;
import scratch_compiler.Compiler.parser.VariableReference;
import scratch_compiler.Compiler.parser.VariableType;

public class TypeDefinition {
    private String name;
    private VariableType type;
    private ArrayList<TypeField> fields;

    public TypeDefinition(String name, VariableType variableType) {
        this.name = name;
        this.type = variableType;
        this.fields = new ArrayList<>();
    }

    public TypeDefinition(String name, ArrayList<TypeField> fields) {
        this.name = name;
        this.type = VariableType.STRUCT;
        this.fields = new ArrayList<>(fields);
    }

    public TypeDefinition(VariableType type) {
        this.name = type.toString();
        this.type = type;
        this.fields = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }

    public TypeDefinition getField(String fieldName) {
        for (TypeField field : fields) {
            if (field.getName().equals(fieldName))
                return field.getType();
        }
        throw new RuntimeException("Field " + fieldName + " not found in struct " + name);
    }

    public ArrayList<TypeField> getFields() {
        return new ArrayList<>(fields);
    }

    public boolean containsField(String fieldName) {
        for (TypeField field : fields) {
            if (field.getName().equals(fieldName))
                return true;
        }
        return false;
    }

    public TypeDefinition reference(VariableReference reference) {
        if (reference == null)
            return this;
        TypeDefinition field = getField(reference.getName());
        if (field == null)
            throw new RuntimeException("Field " + reference.getName() + " not found in struct " + name);

        VariableReference next = reference.getNext();
        return field.reference(next);

    }

    public boolean containsReference(VariableReference reference) {
        if (reference == null)
            return true;

        TypeDefinition field = getField(reference.getName());
        if (field == null)
            return false;

        VariableReference next = reference.getNext();
        return field.containsReference(next);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof TypeDefinition))
            return false;

        TypeDefinition other = (TypeDefinition) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        String out = name;
        return out;
    }

    public static final TypeDefinition INT = new TypeDefinition(VariableType.INT);
    public static final TypeDefinition FLOAT = new TypeDefinition(VariableType.FLOAT);
    public static final TypeDefinition BOOLEAN = new TypeDefinition(VariableType.BOOLEAN);
    public static final TypeDefinition VOID = new TypeDefinition(VariableType.VOID);
    public static final TypeDefinition STRING = new TypeDefinition(VariableType.STRING);

}
