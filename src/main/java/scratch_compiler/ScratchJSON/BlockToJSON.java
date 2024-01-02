package scratch_compiler.ScratchJSON;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Input;
import scratch_compiler.Utils;
import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.StringJSON;
import scratch_compiler.JSON.ToJSON;
import scratch_compiler.Types.Vector2Int;

public class BlockToJSON {
    public static ObjectJSON blocksToJSON(ArrayList<Block> blocks) {
        ObjectJSON json = new ObjectJSON();
        HashMap<Block,String> blockToID = blockToID(blocks);
        HashMap<Block,Vector2Int> blockToPosition = blockToPosition(blocks);

        for (Block block : blocks) {
            json.setObject(blockToID.get(block), blockToJSON(block, blockToID, blockToPosition));
        }
    
        return json;
    }


    private static ObjectJSON blockToJSON(Block block, HashMap<Block, String> blockToID, HashMap<Block, Vector2Int> blockToPosition) {
        ObjectJSON json = new ObjectJSON();
        json.setString("opcode", block.getOpcode());
        json.setObject("inputs", inputsToJSON(block, blockToID));
        json.setObject("fields", new ObjectJSON());
        json.setValue("next", connectedToJSON(block.getNext(), blockToID));
        json.setValue("parent", connectedToJSON(block.getParent(), blockToID));
        json.setBoolean("shadow", false);

        boolean hasParent = block.getParent() != null;
        json.setBoolean("topLevel", !hasParent);
        if (!hasParent) {
            Vector2Int position = blockToPosition.get(block);
            json.setNumber("x", position.x);
            json.setNumber("y", position.y);
        }

        return json;
    }

    private static ObjectJSON inputsToJSON(Block block, HashMap<Block, String> blockToID) {
        ObjectJSON inputs = new ObjectJSON();

        //add support for blocks connected inside the block later

        for(Input input : block.getInputs())
            inputs.setArray(input.getName(), ValueFieldToJSON.valueFieldToJSON(input.getValueField()));
        
        return inputs;
    }

    private static ToJSON connectedToJSON(Block block, HashMap<Block, String> blockToID) {
        if (block == null)
            return null;
        return new StringJSON(blockToID.get(block));
    }

    private static HashMap<Block,String> blockToID(ArrayList<Block> blocks) {
        HashMap<Block,String> blockToID = new HashMap<>();
        ArrayList<String> ids = new ArrayList<>();
        for (Block block : blocks) {
            String id = getBlockID(block, ids);
            ids.add(id);
            blockToID.put(block, id);
        }
        return blockToID;
    }

    private static String getBlockID(Block block, ArrayList<String> ids) {
        String id = "id_block_"+block.getOpcode();
        if (!ids.contains(id))
            return id;

        int i = 2;
        while (ids.contains(id + i))
            i++;
        return id + i;
    }

    private static HashMap<Block,Vector2Int> blockToPosition(ArrayList<Block> blocks) {
        HashMap<Block,Vector2Int> blockToPosition = new HashMap<>();
        Vector2Int position = new Vector2Int(0, 0);
        for (Block block : blocks) {
            blockToPosition.put(block, position);
            position = new Vector2Int(position.x+135, position.y);
        }
        return blockToPosition;
    }
}
