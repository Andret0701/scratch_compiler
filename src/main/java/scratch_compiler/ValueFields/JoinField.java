package scratch_compiler.ValueFields;

public class JoinField extends BinaryOperationField {
    public JoinField(ValueField left, ValueField right) {
        super("operator_join", "STRING1", "STRING2", left, right);
    }
}