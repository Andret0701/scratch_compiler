package scratch_compiler.ScratchJSON;

import scratch_compiler.ScratchVariable;
import scratch_compiler.JSON.ArrayJSON;

public class VariableToJSON {
    
    public static ArrayJSON variableToJSON(ScratchVariable variable) {
        ArrayJSON variableJSON = new ArrayJSON();
        String name = getVariableName(variable);
        variableJSON.addString(name);

        if (variable.isList()) 
            variableJSON.addArray(new ArrayJSON());
         else 
            variableJSON.addString("0");

        return variableJSON;
    }

    public static String getVariableName(ScratchVariable variable) {
        String name="";
        if (variable.isGlobal())
            name+="global_";
        else
            name+="local_";

        name+=variable.getName();
        return name;        
    }

    public static String getVariableId(ScratchVariable variable) {
        String id="id_variable_";
        id+=getVariableName(variable);
        return id;   
    }
}
