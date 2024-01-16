package scratch_compiler.ScratchJSON;

import java.lang.reflect.Array;

import scratch_compiler.Function;
import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.StringJSON;

public class FunctionToJSON {

    public static ObjectJSON functionDataToJSON(Function function){ //a function declaration has three parts: a prototype, the reporter blocks and the definition block
        ObjectJSON functionJSON = new ObjectJSON();
        functionJSON.setObject(getFunctionPrototypeID(function), functionPrototypeToJSON(function));
        for (String argument : function.getFunctionArguments()) 
            functionJSON.setObject(getFunctionArgumentID(argument, function), functionArgumentToJSON(argument,function));

        return functionJSON;
    }
    
    private static ObjectJSON functionPrototypeMutationToJSON(Function function)
    {

        //                                         "argumentnames": "[\"a\",\"b\",\"c\"]",
        //                                         "argumentdefaults": "[\"\",\"\",\"\"]",
        //                                         "children": [],
        //                                         "proccode": "test%s%s%s",
        //                                         "argumentids": "[\"!p1Fmy)s#8ZJ706!icB8\",\"_E{y=VviU+hQn+=sSz%{\",\"3(W)F_VG_eZ{]rJIkAy(\"]",
        //                                         "tagName": "mutation",
        //                                         "warp": "false"
        ObjectJSON mutationJSON = new ObjectJSON();
        mutationJSON.setString("argumentnames", getArgumentNames(function).toJSON());
        mutationJSON.setString("argumentdefaults", getArgumentDefaults(function).toJSON());
        mutationJSON.setArray("children", new ArrayJSON());
        mutationJSON.setString("proccode",  getProccode(function)); 
        mutationJSON.setString("argumentids", getArgumentIds(function).toJSON()); 
        mutationJSON.setString("tagName", "mutation");
        mutationJSON.setBoolean("warp", function.isWarp());
        return mutationJSON;
    }

    private static ArrayJSON getArgumentNames(Function function) {
        ArrayJSON argumentNames = new ArrayJSON();
        for (String input : function.getFunctionArguments()) 
            argumentNames.addString(input);
        return argumentNames;
    }

    private static ArrayJSON getArgumentDefaults(Function function) {
        ArrayJSON argumentDefaults = new ArrayJSON();
        for (String input : function.getFunctionArguments()) 
            argumentDefaults.addString("");
        return argumentDefaults;
    }

    private static String getProccode(Function function) {
        String proccode = function.getName();
        for (int i = 0; i < function.getFunctionArguments().size(); i++)
            proccode += " %s";
        return proccode;
    }

    private static ArrayJSON getArgumentIds(Function function) {
        ArrayJSON argumentIds = new ArrayJSON();
        for (String input : function.getFunctionArguments()) 
            argumentIds.addString(getArgumentID(input, function));
        return argumentIds;
    }

    private static ObjectJSON functionPrototypeInputsToJSON(Function function)
    {
        ObjectJSON functionPrototypeInputsJSON = new ObjectJSON();
        for (String input : function.getFunctionArguments()) 
            functionPrototypeInputsJSON.add(getReference(getArgumentID(input, function),getFunctionArgumentID(input, function)));
        return functionPrototypeInputsJSON;
    }

    public static ObjectJSON functionPrototypeToJSON(Function function)
    {
        ObjectJSON functionPrototypeJSON = new ObjectJSON();
        functionPrototypeJSON.setString("opcode", "procedures_prototype");
        functionPrototypeJSON.setObject("mutation", functionPrototypeMutationToJSON(function));
        functionPrototypeJSON.setBoolean("shadow", true);
        functionPrototypeJSON.setObject("inputs", functionPrototypeInputsToJSON(function));
        functionPrototypeJSON.setBoolean("topLevel", false);
        functionPrototypeJSON.setObject("fields", new ObjectJSON());
        functionPrototypeJSON.setValue("next", null);
        functionPrototypeJSON.setString("parent", getFunctionID(function));
        return functionPrototypeJSON;
    }



    private static ObjectJSON functionArgumentToJSON(String argumentName, Function function)
    {
        ObjectJSON functionArgumentJSON = new ObjectJSON();
        functionArgumentJSON.setString("opcode", "argument_reporter_string_number");
        functionArgumentJSON.setBoolean("shadow", true);
        functionArgumentJSON.setObject("inputs", new ObjectJSON());
        functionArgumentJSON.setBoolean("topLevel", false);

        ObjectJSON fields = new ObjectJSON();
        ArrayJSON argumentNameJSON = new ArrayJSON();
        argumentNameJSON.addString(argumentName);
        argumentNameJSON.addValue(null);
        fields.setArray("VALUE", argumentNameJSON);

        functionArgumentJSON.setObject("fields", fields);
        functionArgumentJSON.setValue("next", null);
        functionArgumentJSON.setString("parent", getFunctionPrototypeID(function));
        return functionArgumentJSON;
    }

    public static String getFunctionID(Function function) {
        String id = "id_function_"+function.getName();
        return id;
    }

    private static String getFunctionPrototypeID(Function function) {
        String id = "id_functionprototype_"+function.getName();
        return id;
    }

    private static String getArgumentID(String argumentName, Function function) {
        String id = "id_argument_"+function.getName();
        id+=":"+argumentName;
        return id;
    }

    private static String getFunctionArgumentID(String argumentName, Function function) {
        String id = "id_functionargument_"+function.getName();
        id+=":"+argumentName;
        return id;
    }

    public static ObjectJSON getFunctionInput(Function function)
    {
        return getReference("custom_block", getFunctionPrototypeID(function));
    }

    private static ObjectJSON getReference(String from, String to) {
        ObjectJSON reference = new ObjectJSON();
        ArrayJSON referenceArray = new ArrayJSON();
        referenceArray.addNumber(1);
        referenceArray.addString(to);
        reference.setArray(from, referenceArray);
        return reference;
    }
}