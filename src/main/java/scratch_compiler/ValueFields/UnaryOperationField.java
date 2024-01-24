package scratch_compiler.ValueFields;


public abstract class UnaryOperationField extends ValueField {
    private String operandName;
    public UnaryOperationField(String opcode, String operandName, ValueField operand) {
        super(opcode);
        this.operandName = operandName;
        setOperand(operand);
    }

    public void setOperand(ValueField operand) {
        setInput(operandName, operand);
    }

}

