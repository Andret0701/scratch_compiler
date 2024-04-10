package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import scratch_compiler.Blocks.PenDownBlock;
import scratch_compiler.Blocks.PenUpBlock;
import scratch_compiler.Blocks.SayBlock;
import scratch_compiler.Blocks.SetPositionBlock;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Compiler.SystemCall;
import scratch_compiler.Compiler.SystemCallFlag;
import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.Variable;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.ScratchValues.DirectionField;
import scratch_compiler.ValueFields.ScratchValues.XPositionField;
import scratch_compiler.ValueFields.ScratchValues.YPositionField;

public class ScratchCoreAssembler {
    private final static ArrayList<SystemCall> systemCalls = new ArrayList<>(Arrays.asList(
            new SystemCall("say", new Type(VariableType.VOID),
                    new ArrayList<>(Arrays.asList(new Variable("message", new Type(VariableType.STRING)))),
                    SystemCallFlag.ChangesGlobalState, SystemCallFlag.OnlyInGlobalScope),
            new SystemCall("wait", new Type(VariableType.VOID),
                    new ArrayList<>(Arrays.asList(new Variable("time", new Type(VariableType.FLOAT)))),
                    SystemCallFlag.ChangesGlobalState, SystemCallFlag.OnlyInGlobalScope),
            new SystemCall("penUp", new Type(VariableType.VOID), new ArrayList<Variable>(),
                    SystemCallFlag.ChangesGlobalState),
            new SystemCall("penDown", new Type(VariableType.VOID), new ArrayList<Variable>(),
                    SystemCallFlag.ChangesGlobalState),
            new SystemCall("penClear", new Type(VariableType.VOID), new ArrayList<Variable>(),
                    SystemCallFlag.ChangesGlobalState),
            new SystemCall("moveTo", new Type(VariableType.VOID),
                    new ArrayList<>(Arrays.asList(new Variable("x", new Type(VariableType.FLOAT)),
                            new Variable("y", new Type(VariableType.FLOAT)))),
                    SystemCallFlag.ChangesGlobalState),
            new SystemCall("getKey", new Type(VariableType.BOOLEAN),
                    new ArrayList<>(Arrays.asList(new Variable("key", new Type(VariableType.STRING))))),
            new SystemCall("sin", new Type(VariableType.FLOAT),
                    new ArrayList<>(Arrays.asList(new Variable("angle", new Type(VariableType.FLOAT))))),
            new SystemCall("cos", new Type(VariableType.FLOAT),
                    new ArrayList<>(Arrays.asList(new Variable("angle", new Type(VariableType.FLOAT))))),
            new SystemCall("sqrt", new Type(VariableType.FLOAT),
                    new ArrayList<>(Arrays.asList(new Variable("value", new Type(VariableType.FLOAT)))))));

    public static ArrayList<SystemCall> getSystemCalls() {
        return new ArrayList<SystemCall>(systemCalls);
    }

    // public static boolean isVariable(String name) {
    // return getDeclarationTable().isVariableDeclared(name);
    // }

    public static boolean isSystemCall(String name) {
        for (SystemCall systemCall : systemCalls) {
            if (systemCall.getName().equals(name))
                return true;
        }
        return false;
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

    // public static BlockStack assembleAssignment(String name, ValueField value) {

    // StackBlock stackBlock;
    // if (name.equals("x"))
    // stackBlock = new SetXBlock(value);
    // else if (name.equals("y"))
    // stackBlock = new SetYBlock(value);
    // else
    // stackBlock = ScratchAssembler.defaultBlock();

    // return new BlockStack(stackBlock);
    // }

    public static BlockStack assembleSystemCall(String name, List<ValueField> arguments) {
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
