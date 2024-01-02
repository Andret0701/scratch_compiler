package scratch_compiler.ScratchJSON;

import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.VariableField;

public class ValueFieldToJSON {
    public static ArrayJSON valueFieldToJSON(ValueField valueField) {
        if (valueField instanceof NumberField)
            return constantToJSON(numberFieldToJSON((NumberField) valueField));
        else if(valueField instanceof StringField)
            return constantToJSON(stringFieldToJSON((StringField) valueField));
        else if(valueField instanceof VariableField)
            return dynamicToJSON(variableFieldToJSON((VariableField) valueField));
        else
            return dynamicToJSON(numberFieldToJSON(new NumberField(0)));
    }

    private static ArrayJSON constantToJSON(ArrayJSON arrayJSON)
    {
        ArrayJSON constant = new ArrayJSON();
        constant.addNumber(1);
        constant.addArray(arrayJSON);
        return constant;
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
}
