package scratch_compiler.Blocks;

import scratch_compiler.ContainerBlock;
import scratch_compiler.ValueFields.NumberField;

public class LoopBlock extends ContainerBlock {
    private NumberField num;

    public LoopBlock(NumberField num) {
        super("control_repeat");
        this.num = num;
    }

    @Override
    public String inputsToJSON() {
        String json = "\"inputs\": {";
        json += "\"TIMES\":" + num.toJSON();

        if (child != null)
            json += ",\"SUBSTACK\": [2, \"" + child.getId() + "\"]";

        json += "}";
        return json;
    }

    @Override
    public String toJSON() {
        String json = super.toJSON();

        String fieldBlockJson = num.blockDataToJSON(id);
        if (fieldBlockJson != "")
            json += "," + fieldBlockJson;

        return json;
    }

}
