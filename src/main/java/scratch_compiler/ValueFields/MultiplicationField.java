package scratch_compiler.ValueFields;

public class MultiplicationField extends BinaryOperationField {
    public MultiplicationField(ValueField left, ValueField right) {
        super("operator_multiply", "NUM1", "NUM2", left, right);
    }

}
