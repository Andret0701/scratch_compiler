package scratch_compiler.ScratchJSON;

import scratch_compiler.ScratchFunction;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.JSON.ObjectJSON;

public class FunctionToJSON {

    public static BlockJSON functionDefinitionToJSON(FunctionDefinitionBlock functionDefinitionBlock) {
        BlockJSON functionDefinitionJSON = BlockToJSON.blockToJSON(functionDefinitionBlock);
        functionDefinitionJSON.getBlock().setObject("inputs", getFunctionInput(functionDefinitionBlock.getFunction()));
        functionDefinitionJSON.addSubBlocks(functionPrototypeToJSON(functionDefinitionJSON.getBlockID(),functionDefinitionBlock.getFunction()).getBlocksJSON());
        for (String input : functionDefinitionBlock.getFunction().getFunctionArguments()) 
            functionDefinitionJSON.addSubBlocks(functionArgumentToJSON(input, functionDefinitionBlock.getFunction()).getBlocksJSON());
        return functionDefinitionJSON;
    }
    
    private static ObjectJSON functionPrototypeMutationToJSON(ScratchFunction function)
    {
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

    public static ObjectJSON functionCallMutationToJSON(ScratchFunction function)
    {
        ObjectJSON mutationJSON = new ObjectJSON();
        mutationJSON.setArray("children", new ArrayJSON());
        mutationJSON.setString("proccode",  getProccode(function)); 
        mutationJSON.setString("argumentids", getArgumentIds(function).toJSON()); 
        mutationJSON.setString("tagName", "mutation");
        mutationJSON.setBoolean("warp", function.isWarp());
        return mutationJSON;
    }

    private static ArrayJSON getArgumentNames(ScratchFunction function) {
        ArrayJSON argumentNames = new ArrayJSON();
        for (String input : function.getFunctionArguments()) 
            argumentNames.addString(input);
        return argumentNames;
    }

    private static ArrayJSON getArgumentDefaults(ScratchFunction function) {
        ArrayJSON argumentDefaults = new ArrayJSON();
        for (String input : function.getFunctionArguments()) 
            argumentDefaults.addString("");
        return argumentDefaults;
    }

    private static String getProccode(ScratchFunction function) {
        String proccode = function.getName();
        for (int i = 0; i < function.getFunctionArguments().size(); i++)
            proccode += " %s";
        return proccode;
    }

    private static ArrayJSON getArgumentIds(ScratchFunction function) {
        ArrayJSON argumentIds = new ArrayJSON();
        for (String input : function.getFunctionArguments()) 
            argumentIds.addString(getArgumentID(input, function));
        return argumentIds;
    }

    private static ObjectJSON functionPrototypeInputsToJSON(ScratchFunction function)
    {
        ObjectJSON functionPrototypeInputsJSON = new ObjectJSON();
        for (String input : function.getFunctionArguments()) 
            functionPrototypeInputsJSON.add(getReference(getArgumentID(input, function),getFunctionArgumentID(input, function)));
        return functionPrototypeInputsJSON;
    }

    public static BlockJSON functionPrototypeToJSON(String functionDefinitionID, ScratchFunction function)
    {
        ObjectJSON functionPrototypeJSON = new ObjectJSON();
        functionPrototypeJSON.setString("opcode", "procedures_prototype");
        functionPrototypeJSON.setObject("mutation", functionPrototypeMutationToJSON(function));
        functionPrototypeJSON.setBoolean("shadow", true);
        functionPrototypeJSON.setObject("inputs", functionPrototypeInputsToJSON(function));
        functionPrototypeJSON.setBoolean("topLevel", false);
        functionPrototypeJSON.setObject("fields", new ObjectJSON());
        functionPrototypeJSON.setValue("next", null);
        functionPrototypeJSON.setString("parent", functionDefinitionID);
        return new BlockJSON(functionPrototypeJSON, getFunctionPrototypeID(function));
    }



    private static BlockJSON functionArgumentToJSON(String argumentName, ScratchFunction function)
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
        return new BlockJSON(functionArgumentJSON, getFunctionArgumentID(argumentName, function));
    }

    private static String getFunctionPrototypeID(ScratchFunction function) {
        String id = "id_functionprototype_"+function.getName();
        return id;
    }

    private static String getArgumentID(String argumentName, ScratchFunction function) {
        String id = "id_argument_"+function.getName();
        id+=":"+argumentName;
        return id;
    }

    private static String getFunctionArgumentID(String argumentName, ScratchFunction function) {
        String id = "id_functionargument_"+function.getName();
        id+=":"+argumentName;
        return id;
    }

    public static ObjectJSON getFunctionInput(ScratchFunction function)
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


    public static ObjectJSON functionCallInputsToJSON(ObjectJSON inputs, ScratchFunction function)
    {
        ObjectJSON functionCallInputsJSON = new ObjectJSON();
        for (String input : inputs.getKeys()) 
            functionCallInputsJSON.setValue(getArgumentID(input,function), inputs.getValue(input));
        return functionCallInputsJSON;
    }
}