package scratch_compiler.ValueFields;

public class SubtractionField extends BinaryOperationField {
    public SubtractionField(ValueField left, ValueField right) {
        super("operator_subtract", "NUM1", "NUM2", left, right);
    }

}
