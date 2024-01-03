package scratch_compiler.ValueFields;

public class DivisionField extends BinaryOperationField {
    public DivisionField(ValueField left, ValueField right) {
        super("operator_divide", "NUM1", "NUM2", left, right);
    }

}
