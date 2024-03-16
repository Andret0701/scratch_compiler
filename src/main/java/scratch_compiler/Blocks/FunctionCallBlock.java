package scratch_compiler.Blocks;

import java.util.ArrayList;

import scratch_compiler.ScratchFunction;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.ValueField;

public class FunctionCallBlock extends StackBlock {
    private ScratchFunction function;
    public FunctionCallBlock(ScratchFunction function)
    {
        this(function, new ArrayList<ValueField>());
    }

    public FunctionCallBlock(ScratchFunction function,ArrayList<ValueField> arguments) {
        super("procedures_call");
        this.function = function;

        ArrayList<String> functionArguments = function.getFunctionArguments();
        if(arguments.size() != functionArguments.size()) 
            throw new IllegalArgumentException("Function call has wrong number of arguments");
     
        for(int i = 0; i < arguments.size(); i++) {
            String argumentName = functionArguments.get(i);
            setInput(argumentName, arguments.get(i));
        }
    }

    public ScratchFunction getFunction() { return function; }

}

// {
//     "next": null,
//     "parent": "id_block_procedures_definition_7",
//     "mutation": {
//             "children": [],
//             "proccode": "hello %s",
//             "argumentids": "[\"id_argument_hello:a\"]",
//             "tagName": "mutation",
//             "warp": "true"
//     },
//     "shadow": false,
//     "inputs": {"id_argument_hello:a": [1,[10,"1"]]},
//     "topLevel": false,
//     "opcode": "procedures_call",
//     "fields": {}
// }