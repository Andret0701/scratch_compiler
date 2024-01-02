package scratch_compiler.ScratchJSON;

import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.Variables.Variable;

public class VariableToJSON {
    
    public static ArrayJSON variableToJSON(Variable variable) {
        ArrayJSON variableJSON = new ArrayJSON();
        String name = getVariableName(variable);
        variableJSON.addString(name);

        if (variable.isList()) {
            variableJSON.addArray(new ArrayJSON());
        } else {
        switch (variable.getType()) {
            case NUMBER:
                variableJSON.addNumber(0);
                break;
            case STRING:
                variableJSON.addString("");
                break;
            case BOOLEAN:
                variableJSON.addBoolean(false);
                break;
            default:
                throw new IllegalArgumentException("Variable type " + variable.getType() + " is not supported");
        }
    }

        
        return variableJSON;
    }

    public static String getVariableName(Variable variable) {
        String name="";
        if (variable.isGlobal())
            name+="global_";
        else
            name+="local_";
        
        name+=variable.getType().toString().toLowerCase()+"_";
        name+=variable.getName();
        return name;        
    }
}
