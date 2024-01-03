package scratch_compiler.ValueFields;

public class ModulusField extends BinaryOperationField {
    public ModulusField(ValueField left, ValueField right) {
        super("operator_mod", "NUM1", "NUM2", left, right);
    }

}
