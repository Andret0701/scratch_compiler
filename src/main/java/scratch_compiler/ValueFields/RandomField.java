package scratch_compiler.ValueFields;

public class RandomField extends ValueField {
    public RandomField(ValueField left, ValueField right) {
        super("operator_random");
        setInput("FROM", left);
        setInput("TO", right);
    }
    // "blocks":
    // {";3GH(JA%S{9e}[}c_;ML": {
    // "next": null,
    // "parent": null,
    // "shadow": false,
    // "inputs": {
    // "FROM": [1.0,[4.0,"1"]],
    // "TO": [1.0,[4.0,"10"]]
    // },
    // "topLevel": true,
    // "x": -607.0,
    // "y": 326.0,
    // "opcode": "operator_random",
    // "fields": {}
    // }},
}
