package scratch_compiler.Compiler;

import java.util.ArrayList;
import java.util.Arrays;

public class SystemCall {
    private String name;
    private Type returnType;
    private ArrayList<Variable> arguments;
    private ArrayList<SystemCallFlag> flags;

    public SystemCall(String name, Type returnType, ArrayList<Variable> arguments, SystemCallFlag... flags) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = new ArrayList<>(arguments);
        this.flags = new ArrayList<>(Arrays.asList(flags));
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public ArrayList<Variable> getArguments() {
        return new ArrayList<Variable>(arguments);
    }

    public ArrayList<SystemCallFlag> getFlags() {
        return new ArrayList<SystemCallFlag>(flags);
    }

    @Override
    public String toString() {
        String args = "";
        for (int i = 0; i < this.arguments.size(); i++) {
            args += this.arguments.get(i).toString();
            if (i < this.arguments.size() - 1)
                args += ", ";
        }

        args = "(" + args + ")";
        return returnType + " " + name + args;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof SystemCall))
            return false;

        SystemCall other = (SystemCall) obj;
        return this.name.equals(other.name) && this.returnType.equals(other.returnType)
                && this.arguments.equals(other.arguments);
    }
}
