package scratch_compiler.Compiler;

public class Variable {
    private String name;
    private Type type;

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable))
            return false;

        Variable variable = (Variable) obj;
        return name.equals(variable.name) && type.equals(variable.type);
    }

}
