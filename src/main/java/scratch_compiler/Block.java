package scratch_compiler;

public class Block {
    private String id;
    private String opcode;
    private Block next;
    private Block parent;

    public Block(String opcode, Block next, Block parent) {
        this.id = Utils.generateID();
        this.opcode = opcode;
        this.next = next;
        this.parent = parent;
    }

    public String nextToJSON() {
        String value = this.next == null ? "null" : this.next.getId();
        return "\"next\": " + value;
    }

    public String parentToJSON() {
        String value = this.parent == null ? "null" : this.parent.getId();
        return "\"parent\": " + value;
    }

    public String toJSON() {
        String json = "\"" + id + "\": {";
        json += "\"opcode\": \"" + opcode + "\",";
        json += nextToJSON() + ",";
        json += parentToJSON() + ",";
        json += "\"inputs\": {},";
        json += "\"fields\": {},";
        json += "\"shadow\": " + false + ",";
        json += "\"topLevel\": " + true + ",";
        json += "\"x\": 695,";
        json += "\"y\": 582";
        json += "}";
        return json;
    }

    public String getId() {
        return id;
    }

    /*
     * "JlP`/`W[4SWi,4qtN}1f": {
     * "opcode": "motion_ifonedgebounce",
     * "next": "P(?=;LX!Q}_T|wTDOG_z",
     * "parent": "Al/er`jzm5nRD:kQ6MWO",
     * "inputs": {},
     * "fields": {},
     * "shadow": false,
     * "topLevel": false
     * "x": 695,
     * "y": 582
     * }
     */
}
