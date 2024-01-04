package scratch_compiler.ValueFields.LogicFields;

import scratch_compiler.ValueFields.ValueField;

public class EqualsField  extends LogicalBinaryOperationField {
    public EqualsField(ValueField left, ValueField right) {
        super("operator_equals", "OPERAND1", "OPERAND2", left, right);
    }

}