package scratch_compiler.ScratchJSON;

import scratch_compiler.JSON.ObjectJSON;

public class BlockJSON {
    private String blockID;
    private ObjectJSON block=null;
    private ObjectJSON subBlocks;   

    public BlockJSON() {
        this.block = null;
        this.subBlocks = new ObjectJSON();
    }

    public BlockJSON(ObjectJSON block, String blockID) {
        this.block = block;
        this.blockID = blockID;
        this.subBlocks = new ObjectJSON();
    }

    public BlockJSON(ObjectJSON block, String blockID, ObjectJSON subBlocks) {
        this.block = block;
        this.blockID = blockID;
        this.subBlocks = subBlocks;
    }

    public void addSubBlocks(ObjectJSON subBlocks) {
        this.subBlocks.add(subBlocks);
    }

    public void setBlock(ObjectJSON block, String blockID) {
        this.block = block;
        this.blockID = blockID;
    }

    public ObjectJSON getBlock() {
        return block;
    }

    public String getBlockID() {
        return blockID;
    }

    public ObjectJSON getSubBlocks() {
        return subBlocks;
    }
    

    public ObjectJSON getBlocksJSON() {
        ObjectJSON blocksJSON = new ObjectJSON();
        if (block != null)
            blocksJSON.setObject(blockID, block);
        blocksJSON.add(subBlocks);
        return blocksJSON;
    }
}
