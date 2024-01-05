package scratch_compiler.Compiler.ScratchAssembler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.SetXBlock;
import scratch_compiler.Blocks.SetYBlock;
import scratch_compiler.Compiler.IdentifierTypes;
import scratch_compiler.Compiler.parser.VariableType;
import scratch_compiler.ValueFields.ValueField;
import scratch_compiler.ValueFields.ScratchValues.DirectionField;
import scratch_compiler.ValueFields.ScratchValues.XPositionField;
import scratch_compiler.ValueFields.ScratchValues.YPositionField;

public class ScratchVariablesAssembler {
    public static IdentifierTypes getIdentiferTypes() {
        IdentifierTypes types = new IdentifierTypes();
        types.add("x", VariableType.FLOAT);
        types.add("y", VariableType.FLOAT);
        types.add("direction", VariableType.FLOAT);
        return types;
    }
    public static boolean isVariable(String name) {
        return getIdentiferTypes().contains(name);
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

    public static Block assembleAssignment(String name, ValueField value) {
        if (name.equals("x"))
            return new SetXBlock(value);
        if (name.equals("y"))
            return new SetYBlock(value);

        return ScratchAssembler.defaultBlock();
    }
}
