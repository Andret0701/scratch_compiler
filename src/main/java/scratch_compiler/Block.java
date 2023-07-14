package scratch_compiler;

public class Block {
    protected String id;
    protected String opcode;
    protected Block next;
    protected Block parent;

    protected int x, y;

    static protected int count = 0;

    public Block(String opcode) {
        this.id = Utils.generateID();
        this.opcode = opcode;
        this.next = null;
        this.parent = null;

        this.x = count * 200;
        this.y = 0;

        count++;
    }

    public void setParent(Block block) {
        if (block == this || parent == block)
            return;
        parent = block;
    }

    public void setNext(Block block) {
        if (block == this || next == block)
            return;
        next = block;
    }

    public void connectTo(Block parent) {
        if (parent == this || this.parent == parent)
            return;

        if (next == parent && parent.parent == this)
            parent.connectTo(null);

        Block oldParent = this.parent;
        this.parent = parent;

        if (oldParent != null)
            oldParent.setNext(null);

        if (parent != null) {
            if (parent.next != null)
                parent.next.setParent(null);
            parent.setNext(this);
        }
    }

    public String nextToJSON() {
        String value = this.next == null ? "null" : "\"" + this.next.getId() + "\"";
        return "\"next\": " + value;
    }

    public String parentToJSON() {
        String value = this.parent == null ? "null" : "\"" + this.parent.getId() + "\"";
        return "\"parent\": " + value;
    }

    public String inputsToJSON() {
        return "\"inputs\": {}";
    }

    public String toJSON() {
        String json = "\"" + id + "\": {";
        json += "\"opcode\": \"" + opcode + "\",";
        json += nextToJSON() + ",";
        json += parentToJSON() + ",";
        json += inputsToJSON() + ",";
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
