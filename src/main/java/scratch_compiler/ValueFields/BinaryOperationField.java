package scratch_compiler.ValueFields;

public abstract class BinaryOperationField extends ValueField {
    private String leftInputName;
    private String rightInputName;

    public BinaryOperationField(String opcode, String leftInputName, String rightInputName, ValueField left,
            ValueField right) {
        super(opcode);
        this.leftInputName = leftInputName;
        this.rightInputName = rightInputName;
        setLeft(left);
        setRight(right);
    }

    public void setLeft(ValueField left) {
        setInput(leftInputName, left);
    }

    public ValueField getLeft() {
        return getInput(leftInputName);
    }

    public void setRight(ValueField right) {
        setInput(rightInputName, right);
    }

    public ValueField getRight() {
        return getInput(rightInputName);
    }

    @Override
    public String toString() {
        return String.format("%s(%s, %s)", getOpcode(), getLeft(), getRight());
    }
}
