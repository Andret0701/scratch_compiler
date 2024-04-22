package scratch_compiler.ValueFields.LogicFields;

import scratch_compiler.ValueFields.ValueField;

public class OrField extends LogicalBinaryOperationField {
    public OrField(ValueField left, ValueField right) {
        super("operator_or", "OPERAND1", "OPERAND2", left, right);
    }
}