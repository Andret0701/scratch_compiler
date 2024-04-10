package scratch_compiler.Blocks;

import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.ValueField;

public class SetPenColorBlock extends StackBlock {
    public SetPenColorBlock(ValueField color) {
        super("pen_setPenColorToColor");
        setInput("COLOR", color);
    }
    // "blocks":
    // {"X1ZrM%?5BLCO?(,l]%lM": {
    // "next": null,
    // "parent": null,
    // "shadow": false,
    // "inputs": {"COLOR": [1.0,[9.0,"#103b54"]]},
    // "topLevel": true,
    // "x": 370.0,
    // "y": 362.0,
    // "opcode": "pen_setPenColorToColor",
    // "fields": {}
    // }},
}