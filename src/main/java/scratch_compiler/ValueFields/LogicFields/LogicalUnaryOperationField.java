package scratch_compiler.ValueFields.LogicFields;

public abstract class LogicalUnaryOperationField extends LogicField {
    private String operandName;
    public LogicalUnaryOperationField(String opcode, String operandName, LogicField operand) {
        super(opcode);
        this.operandName = operandName;
        setOperand(operand);
    }

    public void setOperand(LogicField operand) {
        setInput(operandName, operand);
    }

}
