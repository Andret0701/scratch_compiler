package scratch_compiler.ValueFields.LogicFields;

import scratch_compiler.ValueFields.ValueField;

public class LessThanField  extends LogicalBinaryOperationField {
    public LessThanField(ValueField left, ValueField right) {
        super("operator_lt", "OPERAND1", "OPERAND2", left, right);
    }

}
