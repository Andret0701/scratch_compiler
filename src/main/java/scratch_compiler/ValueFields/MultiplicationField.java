package scratch_compiler.ValueFields;

import scratch_compiler.Utils;

public class MultiplicationField extends NumberOperatorField {
    public MultiplicationField(NumberField num1, NumberField num2) {
        super("operator_multiply", num1, num2);
        id = Utils.generateID();
        this.num1 = num1;
        this.num2 = num2;
    }
}
