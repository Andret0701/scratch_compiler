package scratch_compiler.Compiler;

import java.util.ArrayList;

public class Function {
    private String name;
    private Type returnType;
    private ArrayList<Variable> arguments;

    public Function(String name, Type returnType) {
        this(name, returnType, new ArrayList<Variable>());
    }

    public Function(String name, Type returnType, ArrayList<Variable> arguments) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
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
        if (!(obj instanceof Function))
            return false;
        Function f = (Function) obj;
        return f.name.equals(this.name);
    }
}
