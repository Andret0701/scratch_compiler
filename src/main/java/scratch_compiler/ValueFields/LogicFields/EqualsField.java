package scratch_compiler.ValueFields.LogicFields;

import scratch_compiler.ValueFields.ValueField;

public class EqualsField  extends LogicalBinaryOperationField {
    public EqualsField(ValueField left, ValueField right) {
        super("operator_equals", "NUM1", "NUM2", left, right);
    }

}