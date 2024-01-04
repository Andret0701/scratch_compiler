package scratch_compiler.ValueFields.LogicFields;

import scratch_compiler.ValueFields.ValueField;

public class LogicalBinaryOperationField extends LogicField {
    private String leftInputName;
    private String rightInputName;
    public LogicalBinaryOperationField(String opcode, String leftInputName, String rightInputName, ValueField left, ValueField right) {
        super(opcode);
        this.leftInputName = leftInputName;
        this.rightInputName = rightInputName;
        setLeft(left);
        setRight(right);
    }

    public void setLeft(ValueField left) {
        setChildInput(leftInputName, left);
    }

    public void setRight(ValueField right) {
        setChildInput(rightInputName, right);
    }
}
