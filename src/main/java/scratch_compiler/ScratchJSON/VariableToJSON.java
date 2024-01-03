package scratch_compiler.ScratchJSON;

import scratch_compiler.Variable;
import scratch_compiler.JSON.ArrayJSON;

public class VariableToJSON {
    
    public static ArrayJSON variableToJSON(Variable variable) {
        ArrayJSON variableJSON = new ArrayJSON();
        String name = getVariableName(variable);
        variableJSON.addString(name);

        if (variable.isList()) 
            variableJSON.addArray(new ArrayJSON());
         else 
            variableJSON.addString("0");

        return variableJSON;
    }

    public static String getVariableName(Variable variable) {
        String name="";
        if (variable.isGlobal())
            name+="global_";
        else
            name+="local_";

        name+=variable.getName();
        return name;        
    }

    public static String getVariableId(Variable variable) {
        String id="id_variable_";
        id+=getVariableName(variable);
        return id;   
    }
}
