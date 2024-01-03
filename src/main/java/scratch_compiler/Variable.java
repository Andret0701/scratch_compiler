package scratch_compiler;


public class Variable {
    private String name;
    private boolean isGlobal;
    private boolean isList;

    public Variable(String name,boolean isGlobal) {
        this.name = name;
        this.isGlobal=isGlobal;
        this.isList=false;
    }

    public Variable(String name,boolean isGlobal,boolean isList) {
        this.name = name;
        this.isGlobal=isGlobal;
        this.isList=isList;
    }


    public String getName() {
        return name;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public boolean isList() {
        return isList;
    }

    public Variable clone() {
        return new Variable(name,isGlobal,isList);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Variable))
            return false;
        
        Variable otherVariable = (Variable)other;
        return otherVariable.name.equals(this.name) && otherVariable.isGlobal==this.isGlobal && otherVariable.isList==this.isList;            
    }
}
