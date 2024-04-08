package scratch_compiler.ValueFields.LogicFields;

import scratch_compiler.ValueFields.ValueField;

public class AndField extends LogicalBinaryOperationField {
    public AndField(ValueField left, ValueField right) {
        super("operator_and", "OPERAND1", "OPERAND2", left, right);
    }
}