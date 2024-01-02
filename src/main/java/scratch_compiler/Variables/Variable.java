package scratch_compiler.Variables;


public class Variable {
    private String name;
    private boolean isGlobal;
    private VariableType type;
    private boolean isList;

    public Variable(String name,boolean isGlobal,VariableType type) {
        this.name = name;
        this.isGlobal=isGlobal;
        this.type=type;
        this.isList=false;
    }

    public Variable(String name,boolean isGlobal,VariableType type,boolean isList) {
        this.name = name;
        this.isGlobal=isGlobal;
        this.type=type;
        this.isList=isList;
    }


    public String getName() {
        return name;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public VariableType getType() {
        return type;
    }

    public boolean isList() {
        return isList;
    }

    public Variable clone() {
        return new Variable(name,isGlobal,type,isList);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Variable))
            return false;
        
        Variable otherVariable = (Variable)other;
        return otherVariable.name.equals(this.name) && otherVariable.isGlobal==this.isGlobal && otherVariable.type==this.type && otherVariable.isList==this.isList;            
    }
}
