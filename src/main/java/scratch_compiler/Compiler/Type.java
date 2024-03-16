package scratch_compiler.Compiler;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.VariableType;

public class Type {
    private String name;
    private VariableType type;
    private ArrayList<Variable> fields;

    public Type(String name, ArrayList<Variable> fields) {
        this.name = name;
        this.type = VariableType.STRUCT;
        this.fields = new ArrayList<>(fields);
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }

    public Variable getField(String fieldName) {
        for (Variable field : fields) {
            if (field.getName().equals(fieldName))
                return field;
        }
        return null;
    }

    public ArrayList<Variable> getFields() {
        return fields;
    }

    public boolean containsField(String fieldName) {
        for (Variable field : fields) {
            if (field.getName().equals(fieldName))
                return true;
        }
        return false;
    }

    public Type getFieldType(String fieldName) {
        for (Variable field : fields) {
            if (field.getName().equals(fieldName))
                return field.getType();
        }
        throw new RuntimeException("Field " + fieldName + " not found in struct " + name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof Type) {
            Type other = (Type) obj;
            return name.equals(other.name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public int size() {
        if (type != VariableType.STRUCT)
            return 1;

        int size = 0;
        for (Variable field : fields)
            size += field.getType().size();
        return size;
    }

    @Override
    public String toString() {
        return name.toUpperCase();
    }

    public Type(VariableType type) {
        this.name = type.toString();
        this.type = type;
        this.fields = new ArrayList<>();
    }

    public static final Type INT = new Type(VariableType.INT);
    public static final Type FLOAT = new Type(VariableType.FLOAT);
    public static final Type BOOLEAN = new Type(VariableType.BOOLEAN);
    public static final Type VOID = new Type(VariableType.VOID);
    public static final Type STRING = new Type(VariableType.STRING);

}
