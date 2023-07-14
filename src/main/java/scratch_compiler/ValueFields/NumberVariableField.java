package scratch_compiler.ValueFields;

import scratch_compiler.Variables.NumberVariable;

public class NumberVariableField extends NumberField {
    private NumberVariable variable;

    public NumberVariableField(NumberVariable variable) {
        super(0);
        this.variable = variable;
    }

    @Override
    public String toJSON() {
        return "[3,[12,\"" + variable.getName() + "\",\"" + variable.getId() + "\"],[4,\"" + value + "\"]]";
    }

}
