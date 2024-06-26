package scratch_compiler.ScratchJSON;

import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;
import scratch_compiler.ValueFields.LogicFields.AndField;

public class ValueFieldToJSON {
    public static ArrayJSON valueFieldToJSON(ValueField valueField) {
        // if (valueField.getUnique() == 2089) {
        // AndField and = (AndField) valueField;
        // System.out.println(and.getUnique());
        // System.out.println(and.getFields());
        // System.out.println(and.getInputs());
        // }

        if (valueField == null)
            return dynamicToJSON(defaultJSON());

        if (valueField instanceof NumberField)
            return constantToJSON(numberFieldToJSON((NumberField) valueField));

        if (valueField instanceof StringField)
            return constantToJSON(stringFieldToJSON((StringField) valueField));

        if (valueField instanceof VariableField)
            return dynamicToJSON(variableFieldToJSON((VariableField) valueField));

        return blockReferenceToJSON(valueField);
    }

    private static ArrayJSON constantToJSON(ArrayJSON arrayJSON) {
        ArrayJSON constant = new ArrayJSON();
        constant.addNumber(1);
        constant.addArray(arrayJSON);
        return constant;
    }

    private static ArrayJSON dynamicToJSON(ArrayJSON arrayJSON) {
        ArrayJSON dynamic = new ArrayJSON();
        dynamic.addNumber(3);
        dynamic.addArray(arrayJSON);
        dynamic.addArray(numberFieldToJSON(new NumberField(0)));
        return dynamic;
    }

    private static ArrayJSON blockReferenceToJSON(Block block) {
        ArrayJSON blockJSON = new ArrayJSON();
        blockJSON.addNumber(3);
        blockJSON.addString(BlockToJSON.getBlockID(block));
        blockJSON.addArray(defaultJSON());
        return blockJSON;
    }

    private static ArrayJSON numberFieldToJSON(NumberField numberField) {
        // ArrayJSON arrayJSON = new ArrayJSON();
        // arrayJSON.addNumber(4);
        // arrayJSON.addString(numberField.getValue()+"");
        // return arrayJSON;
        return stringFieldToJSON(new StringField(numberField.getValue()));
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
        return stringFieldToJSON(new StringField(""));
    }
}
