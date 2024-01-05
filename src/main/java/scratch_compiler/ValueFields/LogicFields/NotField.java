package scratch_compiler.ValueFields.LogicFields;

public class NotField extends LogicalUnaryOperationField {
    public NotField(LogicField operand) {
        super("operator_not", "OPERAND", operand);
    }
}
