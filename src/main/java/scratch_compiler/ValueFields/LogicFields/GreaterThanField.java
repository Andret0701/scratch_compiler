package scratch_compiler.ValueFields.LogicFields;

import scratch_compiler.ValueFields.ValueField;

public class GreaterThanField  extends LogicalBinaryOperationField {
    public GreaterThanField(ValueField left, ValueField right) {
        super("operator_gt", "OPERAND1", "OPERAND2", left, right);
    }

}
