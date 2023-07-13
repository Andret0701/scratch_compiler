package scratch_compiler;

public class Block {
    private String id;
    private String opcode;
    private Block next;
    private Block parent;

    private int x, y;

    static private int count = 0;

    public Block(String opcode) {
        this.id = Utils.generateID();
        this.opcode = opcode;
        this.next = null;
        this.parent = null;

        this.x = count * 100;
        this.y = 0;

        count++;
    }

    public void setParent(Block block) {
        if (block == this || parent == block)
            return;

        Block oldParent = this.parent;
        this.parent = block;

        if (oldParent != null)
            oldParent.setNext(null);

        if (block != null)
            block.setNext(this);
    }

    public void setNext(Block block) {
        if (block == this || next == block)
            return;

        Block oldNext = this.next;
        this.next = block;

        if (oldNext != null)
            oldNext.setParent(null);

        if (block != null)
            block.setParent(this);
    }

    public String nextToJSON() {
        String value = this.next == null ? "null" : "\"" + this.next.getId() + "\"";
        return "\"next\": " + value;
    }

    public String parentToJSON() {
        String value = this.parent == null ? "null" : "\"" + this.parent.getId() + "\"";
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
        json += "\"topLevel\": " + (parent == null);

        if (parent == null) {
            json += ",";
            json += "\"x\": " + x + ",";
            json += "\"y\": " + y;
        }

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
