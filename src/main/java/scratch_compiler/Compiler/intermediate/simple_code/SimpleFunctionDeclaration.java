package scratch_compiler.Compiler.intermediate.simple_code;

import java.util.ArrayList;

import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;

public class SimpleFunctionDeclaration extends Statement {
    private String name;
    private ArrayList<VariableType> arguments;
    private ArrayList<VariableType> returnTypes;
    private Scope scope;

    public SimpleFunctionDeclaration(String name, ArrayList<VariableType> arguments,
            ArrayList<VariableType> returnTypes, Scope scope) {
        this.name = name;
        this.arguments = arguments;
        this.returnTypes = returnTypes;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public ArrayList<VariableType> getArguments() {
        return new ArrayList<>(arguments);
    }

    public ArrayList<VariableType> getReturnTypes() {
        return new ArrayList<>(returnTypes);
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        String returns = "";
        for (int i = 0; i < this.returnTypes.size(); i++) {
            returns += this.returnTypes.get(i).toString();
            if (i < this.returnTypes.size() - 1)
                returns += ", ";
        }

        String args = "";
        for (int i = 0; i < this.arguments.size(); i++) {
            args += this.arguments.get(i).toString();
            if (i < this.arguments.size() - 1)
                args += ", ";
        }
        args = "(" + args + ")";

        return returns + " " + name + args + " " + scope;
    }
}