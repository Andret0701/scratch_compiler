package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.ValueFields.FunctionArgumentField;


public class Function {
    private String name;
    private ArrayList<String> arguments;
    private boolean isWarp;
    public Function(String name, boolean isWarp, ArrayList<String> arguments) {
        this.name = name;
        this.isWarp = isWarp;
        this.arguments = new ArrayList<>(arguments);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFunctionArguments() {
        return arguments;
    }

    public boolean isWarp() {
        return isWarp;
    }

    public boolean containsArgument(String argument) {
        return arguments.contains(argument);
    }

    

    public FunctionArgumentField getArgumentField(String argument) {
        if (!containsArgument(argument))
            throw new IllegalArgumentException("Function does not contain argument: " + argument);
        return new FunctionArgumentField(this, argument);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Function) {
            Function function = (Function) obj;
            return function.getName().equals(name) && function.getFunctionArguments().equals(arguments);
        }
        return false;
    }

}