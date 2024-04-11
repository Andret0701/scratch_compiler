package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;
import java.util.Stack;

import scratch_compiler.Compiler.Function;
import scratch_compiler.ScratchFunction;
import scratch_compiler.ScratchProgram;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.intermediate.IntermediateCode;
import scratch_compiler.Compiler.intermediate.simple_code.SimpleFunctionDeclaration;
import scratch_compiler.Compiler.optimiser.Optimizer;
import scratch_compiler.Blocks.AddListBlock;
import scratch_compiler.Blocks.ChangeListBlock;
import scratch_compiler.Blocks.ClearListBlock;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
import scratch_compiler.Blocks.RemoveListBlock;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.HatBlock;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.CompiledCode;
import scratch_compiler.Compiler.Compiler;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.Compiler.parser.expressions.BinaryOperator;
import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.UnaryOperator;
import scratch_compiler.Compiler.parser.expressions.types.OperatorType;
import scratch_compiler.Compiler.parser.expressions.values.BooleanValue;
import scratch_compiler.Compiler.parser.expressions.values.FloatValue;
import scratch_compiler.Compiler.parser.expressions.values.IntValue;
import scratch_compiler.Compiler.parser.expressions.values.StringValue;
import scratch_compiler.Compiler.parser.expressions.values.VariableValue;
import scratch_compiler.Compiler.parser.statements.Assignment;
import scratch_compiler.Compiler.parser.statements.FunctionCall;
import scratch_compiler.Compiler.parser.statements.FunctionDeclaration;
import scratch_compiler.Compiler.parser.statements.IfStatement;
import scratch_compiler.Compiler.parser.statements.ReturnStatement;
import scratch_compiler.Compiler.parser.statements.VariableDeclaration;
import scratch_compiler.Compiler.parser.statements.WhileStatement;
import scratch_compiler.Compiler.scratchIntermediate.ConvertToScratchIntermediate;
import scratch_compiler.Compiler.parser.statements.Scope;
import scratch_compiler.Compiler.parser.statements.Statement;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.FunctionArgumentField;
import scratch_compiler.ValueFields.JoinField;
import scratch_compiler.ValueFields.ListElementField;
import scratch_compiler.ValueFields.ListLengthField;
import scratch_compiler.ValueFields.ModulusField;
import scratch_compiler.ValueFields.MultiplicationField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.StringField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.LogicFields.EqualsField;
import scratch_compiler.ValueFields.LogicFields.GreaterThanField;
import scratch_compiler.ValueFields.LogicFields.LessThanField;

public class ScratchAssembler {
    public static ScratchProgram assemble(String code) {
        IntermediateCode intermediateCode = Compiler.compile(code, ScratchCoreAssembler.getSystemCalls(), true);
        // System.out.println(intermediateCode);
        intermediateCode = ConvertToScratchIntermediate.convert(intermediateCode);
        intermediateCode = Optimizer.optimize(intermediateCode);
        // System.out.println(intermediateCode);

        // ArrayList<FunctionDeclaration> functionDeclarations =
        // compiledCode.getFunctions();

        BlockStack blockStack = new BlockStack();
        blockStack.push(ScopeAssembler.assemble(intermediateCode.getGlobalScope()));
        // blockStack.push(StackAssembler.initializeStack());

        ArrayList<HatBlock> functionDefinitionBlocks = new ArrayList<>();
        for (SimpleFunctionDeclaration functionDeclaration : intermediateCode.getFunctions()) {
            FunctionDefinitionBlock functionDefinitionBlock = FunctionAssembler.assembleFunction(functionDeclaration);
            functionDefinitionBlocks.add(functionDefinitionBlock);
        }

        return new ScratchProgram(blockStack, functionDefinitionBlocks);
    }

}
