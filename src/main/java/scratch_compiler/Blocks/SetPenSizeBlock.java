package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.ValueField;

public class SetPenSizeBlock extends StackBlock {
    // "blocks":
    // {"-Rh7[w87.60)R0e$ub=Y": {
    // "next": null,
    // "parent": null,
    // "shadow": false,
    // "inputs": {"SIZE": [1.0,[4.0,"50"]]},
    // "topLevel": true,
    // "x": -96.0,
    // "y": 616.0,
    // "opcode": "pen_setPenSizeTo",
    // "fields": {}
    // }},

    public SetPenSizeBlock(ValueField size) {
        super("pen_setPenSizeTo");
        setInput("SIZE", size);
    }

}
