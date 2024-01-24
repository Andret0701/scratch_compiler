package scratch_compiler.Blocks;

import java.util.ArrayList;

import scratch_compiler.Function;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.ValueFields.ValueField;

public class FunctionCallBlock extends StackBlock {
    public FunctionCallBlock(Function function,ArrayList<ValueField> arguments) {
        super("procedures_call");//, function);

        ArrayList<String> functionArguments = function.getFunctionArguments();
        if(arguments.size() != functionArguments.size()) 
            throw new IllegalArgumentException("Function call has wrong number of arguments");
     
        for(int i = 0; i < arguments.size(); i++) {
            String argumentName = functionArguments.get(i);
            setInput(argumentName, arguments.get(i));
        }
    }

}