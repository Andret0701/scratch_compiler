package scratch_compiler.ScratchJSON;

import java.util.ArrayList;

import scratch_compiler.Variable;
import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.ScratchObjects.Background;
import scratch_compiler.ScratchObjects.Figure;

public class ScratchObjectToJSON {
    public static ObjectJSON figureToJSON(Figure figure) {
        ObjectJSON figureJSON = new ObjectJSON();
        figureJSON.setString("name", figure.getName());
        figureJSON.setBoolean("isStage", false);
        figureJSON.setObject("variables", variablesToJSON(figure.getLocalVariables()));
        figureJSON.setObject("lists", listsToJSON(figure.getLocalVariables()));
        figureJSON.setObject("broadcasts", new ObjectJSON());
        figureJSON.setObject("blocks", BlockToJSON.blocksToJSON(figure.getBlocks()));
        figureJSON.setObject("comments", new ObjectJSON());
        figureJSON.setNumber("currentCostume", 0);
        figureJSON.setArray("costumes",CostumeToJSON.costumesToJSON(figure.getCostumes(),false)); 
        figureJSON.setArray("sounds",new ArrayJSON());
        figureJSON.setNumber("volume", 100);
        figureJSON.setNumber("layerOrder", 1);
        figureJSON.setBoolean("visible", figure.isVisible());
        figureJSON.setNumber("x", figure.getPosition().x);
        figureJSON.setNumber("y", figure.getPosition().y);
        figureJSON.setNumber("size", figure.getSize());
        figureJSON.setNumber("direction", figure.getDirection());
        figureJSON.setBoolean("draggable", false);
        figureJSON.setString("rotationStyle", "all around");
        return figureJSON;
    }


    public static ObjectJSON backgroundToJSON(Background background, ArrayList<Variable> globalVariables) {
        ObjectJSON backgroundJSON = new ObjectJSON();
        backgroundJSON.setString("name", background.getName());
        backgroundJSON.setBoolean("isStage", true);
        ArrayList<Variable> variables = new ArrayList<>(globalVariables);
        variables.addAll(background.getLocalVariables());
        backgroundJSON.setObject("variables", variablesToJSON(variables));
        backgroundJSON.setObject("lists", listsToJSON(variables));
        backgroundJSON.setObject("broadcasts", new ObjectJSON());
        backgroundJSON.setObject("blocks", BlockToJSON.blocksToJSON(background.getBlocks()));
        backgroundJSON.setObject("comments", new ObjectJSON());
        backgroundJSON.setNumber("currentCostume", 0);
        backgroundJSON.setArray("costumes",CostumeToJSON.costumesToJSON(background.getCostumes(),true));
        backgroundJSON.setArray("sounds",new ArrayJSON());
        // backgroundJSON.setValue("currentCostume", new RemoveLaterJSON("0,\n" +
        //         "            \"costumes\": [\n" +
        //         "                {\n" +
        //         "                    \"assetId\": \"bcf454acf82e4504149f7ffe07081dbc\",\n" +
        //         "                    \"name\": \"drakt1\",\n" +
        //         "                    \"bitmapResolution\": 1,\n" +
        //         "                    \"md5ext\": \"bcf454acf82e4504149f7ffe07081dbc.svg\",\n" +
        //         "                    \"dataFormat\": \"svg\",\n" +
        //         "                    \"rotationCenterX\": 48,\n" +
        //         "                    \"rotationCenterY\": 50\n" +
        //         "                },\n" +
        //         "                {\n" +
        //         "                    \"assetId\": \"0fb9be3e8397c983338cb71dc84d0b25\",\n" +
        //         "                    \"name\": \"drakt2\",\n" +
        //         "                    \"bitmapResolution\": 1,\n" +
        //         "                    \"md5ext\": \"0fb9be3e8397c983338cb71dc84d0b25.svg\",\n" +
        //         "                    \"dataFormat\": \"svg\",\n" +
        //         "                    \"rotationCenterX\": 46,\n" +
        //         "                    \"rotationCenterY\": 53\n" +
        //         "                }\n" +
        //         "            ],\n" +
        //         "            \"sounds\": [\n" +
        //         "                {\n" +
        //         "                    \"assetId\": \"83c36d806dc92327b9e7049a565c6bff\",\n" +
        //         "                    \"name\": \"Mjau\",\n" +
        //         "                    \"dataFormat\": \"wav\",\n" +
        //         "                    \"format\": \"\",\n" +
        //         "                    \"rate\": 48000,\n" +
        //         "                    \"sampleCount\": 40681,\n" +
        //         "                    \"md5ext\": \"83c36d806dc92327b9e7049a565c6bff.wav\"\n" +
        //         "                }\n" +
        //         "            ],\n" +
        //         "            \"volume\": 100,\n" +
        //         "            \"layerOrder\": 0"));
        backgroundJSON.setNumber("volume", 100);
        backgroundJSON.setNumber("layerOrder", 0);
        backgroundJSON.setNumber("tempo", 60);
        backgroundJSON.setNumber("videoTransparency", 50);
        backgroundJSON.setString("videoState", "on");
        backgroundJSON.setValue("textToSpeechLanguage", null);

        return backgroundJSON;
    }


    private static ObjectJSON variablesToJSON(ArrayList<Variable> variables) {
        ObjectJSON variablesJSON = new ObjectJSON();
        for (Variable variable : variables) 
        {
            if(!variable.isList())
                variablesJSON.setValue(VariableToJSON.getVariableId(variable), VariableToJSON.variableToJSON(variable));
        }
        return variablesJSON;
    }

    private static ObjectJSON listsToJSON(ArrayList<Variable> variables) {
        ObjectJSON variablesJSON = new ObjectJSON();
        for (Variable variable : variables) 
        {
            if(variable.isList())
                variablesJSON.setValue(VariableToJSON.getVariableId(variable), VariableToJSON.variableToJSON(variable));
        }
        return variablesJSON;
    }

}
