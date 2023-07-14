package scratch_compiler.ValueFields;

import scratch_compiler.Utils;

public class NumberOperatorField extends NumberField {
    protected String id;
    protected String opcode;
    protected NumberField num1;
    protected NumberField num2;

    public NumberOperatorField(String opcode, NumberField num1, NumberField num2) {
        super(0);
        id = Utils.generateID();
        this.opcode = opcode;
        this.num1 = num1;
        this.num2 = num2;
    }

    public String toJSON() {
        return "[3,\"" + id + "\",[4,\"" + value + "\"]]";
    }

    private String inputToJSON() {
        String json = "\"inputs\": {";
        json += "\"NUM1\":" + num1.toJSON() + ",";
        json += "\"NUM2\":" + num2.toJSON();
        json += "}";
        return json;
    }

    public String blockDataToJSON(String parent_id) {
        String json = "\"" + id + "\":{";
        json += "\"opcode\":\"" + opcode + "\",";
        json += "\"next\":null,";
        json += "\"parent\":\"" + parent_id + "\",";
        json += inputToJSON();
        json += ",";
        json += "\"fields\":{},";
        json += "\"shadow\":false,";
        json += "\"topLevel\":false";
        json += "}";

        String num1JSON = num1.blockDataToJSON(id);
        if (num1JSON != "")
            json += "," + num1JSON;

        String num2JSON = num2.blockDataToJSON(id);
        if (num2JSON != "")
            json += "," + num2JSON;

        return json;
    }

}
