package scratch_compiler.ValueFields;

public class RoundField extends UnaryOperationField {
    public RoundField(ValueField input) {
        super("operator_round", "NUM", input);
    }
    
}
