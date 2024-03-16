package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import scratch_compiler.Blocks.PenDownBlock;
import scratch_compiler.Blocks.PenUpBlock;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.SetPositionBlock;
import scratch_compiler.Blocks.SetXBlock;
import scratch_compiler.Blocks.SetYBlock;
import scratch_compiler.Blocks.WaitBlock;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.Compiler.DeclarationTable;
import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.ScratchValues.DirectionField;
import scratch_compiler.ValueFields.ScratchValues.XPositionField;
import scratch_compiler.ValueFields.ScratchValues.YPositionField;

public class ScratchCoreAssembler {
    public static DeclarationTable getDeclarationTable() {
        DeclarationTable declarationTable = DeclarationTable.loadDeclarationTable();
        declarationTable.declareVariable(new Variable("x", new Type(VariableType.FLOAT)));
        declarationTable.declareVariable(new Variable("y", new Type(VariableType.FLOAT)));
        declarationTable.declareVariable(new Variable("direction", new Type(VariableType.FLOAT)));

        declarationTable.declareFunction(new Function("say", new Type(VariableType.VOID),
                new Variable("message", new Type(VariableType.STRING))));
        declarationTable.declareFunction(new Function("penUp", new Type(VariableType.VOID)));
        declarationTable.declareFunction(new Function("penDown", new Type(VariableType.VOID)));
        declarationTable.declareFunction(new Function("moveTo", new Type(VariableType.VOID),
                new Variable("x", new Type(VariableType.FLOAT)),
                new Variable("y", new Type(VariableType.FLOAT))));

        return declarationTable;
    }

    public static boolean isVariable(String name) {
        return getDeclarationTable().isVariableDeclared(name);
    }

    public static boolean isFunction(String name) {
        return getDeclarationTable().isFunctionDeclared(name);
    }

    public static ValueField assembleExpression(String name) {
        if (name.equals("x"))
            return new XPositionField();
        if (name.equals("y"))
            return new YPositionField();
        if (name.equals("direction"))
            return new DirectionField();
        return ScratchAssembler.defaultField();
    }

    public static BlockStack assembleAssignment(String name, ValueField value) {

        StackBlock stackBlock;
        if (name.equals("x"))
            stackBlock = new SetXBlock(value);
        else if (name.equals("y"))
            stackBlock = new SetYBlock(value);
        else
            stackBlock = ScratchAssembler.defaultBlock();

        return new BlockStack(stackBlock);
    }

    public static BlockStack assembleFunctionCall(String name, List<ValueField> arguments) {
        if (name.equals("say"))
            return new BlockStack(new SayBlock(arguments.get(0)));
        if (name.equals("penUp"))
            return new BlockStack(new PenUpBlock());
        if (name.equals("penDown"))
            return new BlockStack(new PenDownBlock());
        if (name.equals("moveTo"))
            return new BlockStack(new SetPositionBlock(arguments.get(0), arguments.get(1)));

        return new BlockStack(ScratchAssembler.defaultBlock());
    }
}
