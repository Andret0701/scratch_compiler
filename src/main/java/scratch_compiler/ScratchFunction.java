package scratch_compiler;

import java.util.ArrayList;


public class ScratchFunction {
    private String name;
    private ArrayList<String> arguments;
    private boolean isWarp;
    public ScratchFunction(String name, boolean isWarp, ArrayList<String> arguments) {
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ScratchFunction) {
            ScratchFunction function = (ScratchFunction) obj;
            return function.getName().equals(name) && function.getFunctionArguments().equals(arguments);
        }
        return false;
    }
}