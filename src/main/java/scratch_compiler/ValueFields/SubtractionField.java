package scratch_compiler.ValueFields;

import scratch_compiler.Utils;

public class SubtractionField extends NumberOperatorField {
    public SubtractionField(NumberField num1, NumberField num2) {
        super("operator_subtract", num1, num2);
        id = Utils.generateID();
        this.num1 = num1;
        this.num2 = num2;
    }
}