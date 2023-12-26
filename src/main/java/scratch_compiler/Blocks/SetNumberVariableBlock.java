package scratch_compiler.Blocks;

import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.Variables.NumberVariable;

public class SetNumberVariableBlock extends Block {
    NumberVariable variable;
    NumberField num;

    public SetNumberVariableBlock(NumberVariable variable, NumberField num) {
        super("data_setvariableto");
        this.variable = variable;
        this.num = num;
    }

    @Override
    public String inputsToJSON() {
        String json = "\"inputs\": {";
        json += "\"VALUE\": " + num.toJSON();
        json += "}";
        return json;
    }

    @Override
    public String fieldsToJSON() {
        String json = "\"fields\": {";
        json += "\"VARIABLE\": [\"" + variable.getName() + "\", \"" + variable.getId() + "\"]";
        json += "}";
        return json;
    }

}
