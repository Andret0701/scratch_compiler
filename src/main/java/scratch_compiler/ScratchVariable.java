package scratch_compiler;


public class ScratchVariable {
    private String name;
    private boolean isGlobal;
    private boolean isList;

    public ScratchVariable(String name,boolean isGlobal) {
        this.name = name;
        this.isGlobal=isGlobal;
        this.isList=false;
    }

    public ScratchVariable(String name,boolean isGlobal,boolean isList) {
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

    public ScratchVariable clone() {
        return new ScratchVariable(name,isGlobal,isList);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ScratchVariable))
            return false;
        
        ScratchVariable otherVariable = (ScratchVariable)other;
        return otherVariable.name.equals(this.name) && otherVariable.isGlobal==this.isGlobal && otherVariable.isList==this.isList;            
    }
}
