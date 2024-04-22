package scratch_compiler.Compiler.parser;

public enum VariableType {
    STRING,
    FLOAT,
    INT,
    BOOL,
    STRUCT,
    VOID;

    @Override
    public String toString() {
        switch (this) {
            case STRING:
                return "string";
            case FLOAT:
                return "float";
            case INT:
                return "int";
            case BOOL:
                return "bool";
            case STRUCT:
                return "struct";
            case VOID:
                return "void";
            default:
                return null;
        }
    }
}
