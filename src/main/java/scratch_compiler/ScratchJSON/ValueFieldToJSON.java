package scratch_compiler.ScratchJSON;

import java.util.HashMap;

import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.Blocks.Block;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;

public class ValueFieldToJSON {
    public static ArrayJSON valueFieldToJSON(ValueField valueField, HashMap<Block, String> blockToId) {
        if(valueField==null)
            return dynamicToJSON(defaultJSON());

        if (valueField instanceof NumberField)
            return constantToJSON(numberFieldToJSON((NumberField) valueField));
        
        if(valueField instanceof StringField)
            return constantToJSON(stringFieldToJSON((StringField) valueField));
        
        if(valueField instanceof VariableField)
            return dynamicToJSON(variableFieldToJSON((VariableField) valueField));
        
        if (valueField.getBlock() != null)
            return blockToJSON(valueField.getBlock(), blockToId);
        
        return dynamicToJSON(defaultJSON());
    }

    private static ArrayJSON constantToJSON(ArrayJSON arrayJSON)
    {
        ArrayJSON constant = new ArrayJSON();
        constant.addNumber(1);
        constant.addArray(arrayJSON);
        return constant;
    }

    private static ArrayJSON blockToJSON(Block block, HashMap<Block, String> blockToId)
    {
        ArrayJSON blockJSON = new ArrayJSON();
        blockJSON.addNumber(3);
        blockJSON.addString(blockToId.get(block));
        blockJSON.addArray(defaultJSON());
        return blockJSON;
    }

    private static ArrayJSON dynamicToJSON(ArrayJSON arrayJSON)
    {
        ArrayJSON dynamic = new ArrayJSON();
        dynamic.addNumber(3);
        dynamic.addArray(arrayJSON);
        dynamic.addArray(numberFieldToJSON(new NumberField(0)));
        return dynamic;
    }

    private static ArrayJSON numberFieldToJSON(NumberField numberField) {
        ArrayJSON arrayJSON = new ArrayJSON();
        arrayJSON.addNumber(4);
        arrayJSON.addString(numberField.getValue()+"");
        return arrayJSON;
    }

    private static ArrayJSON stringFieldToJSON(StringField stringField) {
        ArrayJSON arrayJSON = new ArrayJSON();
        arrayJSON.addNumber(10);
        arrayJSON.addString(stringField.getValue());
        return arrayJSON;
    }

    private static ArrayJSON variableFieldToJSON(VariableField variableField) {
        ArrayJSON arrayJSON = new ArrayJSON();
        arrayJSON.addNumber(12);
        arrayJSON.addString(VariableToJSON.getVariableName(variableField.getVariable()));
        arrayJSON.addString(VariableToJSON.getVariableId(variableField.getVariable()));
        return arrayJSON;
    }

    private static ArrayJSON defaultJSON() {
        return numberFieldToJSON(new NumberField(0));
    }
}
