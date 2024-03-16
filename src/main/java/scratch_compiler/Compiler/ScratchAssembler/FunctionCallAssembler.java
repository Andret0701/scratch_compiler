package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;

import scratch_compiler.ScratchFunction;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.FunctionCallBlock;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.ValueFields.ValueField;

public class FunctionCallAssembler {
        public static BlockStack assemble(FunctionCall functionCall, VariableStackReference stack, boolean isFunction) {
            ArrayList<ValueField> arguments = new ArrayList<ValueField>();
            for (int i = 0; i < functionCall.getArguments().size(); i++) {
                arguments.add(ScratchAssembler.assembleExpression(functionCall.getArguments().get(i), stack,isFunction));
            }

            ScratchFunction function = FunctionAssembler.compilerFunctionToScratchFunction(functionCall.getFunction());
            String name = function.getName();
            if (ScratchCoreAssembler.isFunction(name))
                return ScratchCoreAssembler.assembleFunctionCall(name, arguments);

        
            stack.addScope();

            BlockStack stackBlock = new BlockStack();
            for (int i = 0; i < arguments.size(); i++) {
                String argumentName = ":argument"+i;
                stack.addVariable(new ScratchVariable(argumentName, false));
                stackBlock.push(StackAssembler.addValueToStack(arguments.get(i)));
            }

            ArrayList<ValueField> argumentFields = new ArrayList<ValueField>();
            argumentFields.add(new SubtractionField(StackAssembler.lengthOfStack(), new NumberField(arguments.size()-1)));
            stackBlock.push(new FunctionCallBlock(function, argumentFields));
            
            stack.removeScope();
            return stackBlock;

            //f1(f2(x))*(f3(x)+f4(x))

            //f2(x)
            //f1(f2)
            //f3(x)
            //f4(x)
            //f3+f4
            //f1*(f3+f4)
        }

}


