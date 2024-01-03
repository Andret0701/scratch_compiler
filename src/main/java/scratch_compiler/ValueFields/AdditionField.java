package scratch_compiler.ValueFields;

public class AdditionField extends BinaryOperationField {
    public AdditionField(ValueField left, ValueField right) {
        super("operator_add", "NUM1", "NUM2", left, right);
    }

}
