package scratch_compiler.Blocks;

import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.ValueFields.NumberField;

public class MoveBlock extends Block {
    private NumberField steps;

    public MoveBlock(NumberField steps) {
        super("motion_movesteps");
        this.steps = steps;
    }

    @Override
    public String inputsToJSON() {
        String json = "\"inputs\": {";
        json += "\"STEPS\":" + steps.toJSON();
        json += "}";
        return json;
    }

    @Override
    public String toJSON() {
        String json = super.toJSON();

        String fieldBlockJson = steps.blockDataToJSON(id);
        if (fieldBlockJson != "")
            json += "," + fieldBlockJson;

        return json;
    }

}
