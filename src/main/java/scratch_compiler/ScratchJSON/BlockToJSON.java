package scratch_compiler.ScratchJSON;

import scratch_compiler.Field;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Input;
import scratch_compiler.Variable;
import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.StringJSON;
import scratch_compiler.JSON.ToJSON;
import scratch_compiler.Blocks.Block;
import scratch_compiler.Blocks.Function;
import scratch_compiler.Types.Vector2Int;
import scratch_compiler.ValueFields.ValueField;
public class BlockToJSON {
    public static ObjectJSON blocksToJSON(ArrayList<Block> blocks) {
        ObjectJSON json = new ObjectJSON();
        HashMap<Block,Vector2Int> blockToPosition = blockToPosition(blocks);
        
        blocks = getAllBlocks(blocks);
        HashMap<Block,String> blockToID = blockToID(blocks);
        for (Block block : blocks) {
            json.setObject(blockToID.get(block), blockToJSON(block, blockToID, blockToPosition));
            if(block instanceof Function)
                json.add(FunctionToJSON.functionDataToJSON((Function) block));
        }
    
        return json;
    }

    private static ArrayList<Block> getAllBlocks(ArrayList<Block> blocks) {
        ArrayList<Block> allBlocks = new ArrayList<>();
        for (Block block : blocks) {
            allBlocks.addAll(block.getBlocks());
        }
        return allBlocks;
    }


    private static ObjectJSON blockToJSON(Block block, HashMap<Block, String> blockToID, HashMap<Block, Vector2Int> blockToPosition) {
        ObjectJSON json = new ObjectJSON();
        json.setString("opcode", block.getOpcode());
        json.setObject("inputs", inputsToJSON(block, blockToID));
        json.setObject("fields", fieldsToJSON(block));
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
        if (block instanceof Function)
            return FunctionToJSON.getFunctionInput((Function) block);

        ObjectJSON inputs = new ObjectJSON();

        ArrayList<Block> insideChildren = block.getInsideChildren();
        for (int i = 0; i < insideChildren.size(); i++) {
            if (insideChildren.get(i) == null)
                continue;

            String name = "SUBSTACK";
            if (i > 0)
                name += i+1;

            ArrayJSON substack = new ArrayJSON();
            substack.addNumber(2);
            substack.addString(blockToID.get(insideChildren.get(i)));
            inputs.setArray(name, substack);
        }

        for(Input input : block.getInputs())
            inputs.setArray(input.getName(), ValueFieldToJSON.valueFieldToJSON(input.getValueField(), blockToID));
        
        return inputs;
    }

    private static ObjectJSON fieldsToJSON(Block block) {
        ObjectJSON fields = new ObjectJSON();
        for (Field field : block.getFields())
        {
            ArrayJSON fieldJSON = new ArrayJSON();
            if (field.getVariable() !=null)
            {   
                Variable variable = field.getVariable();
                fieldJSON.addString(VariableToJSON.getVariableName(variable));
                fieldJSON.addString(VariableToJSON.getVariableId(variable));
                fields.setArray(field.getName(), fieldJSON);
            }
            else if (field.getType() !=null)
            {
                fieldJSON.addString(field.getType());
                fieldJSON.addValue(null);
                fields.setArray(field.getName(), fieldJSON);
            }
            else if (field.getValueField() instanceof ValueField)
            {
                throw new RuntimeException("Field not implemented: "+field.getValueField().getClass().getName());
            }
            else
                throw new RuntimeException("Field not supported: "+field.getValueField().getClass().getName());
        }
        return fields;
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
        if (block instanceof Function)
            return FunctionToJSON.getFunctionID((Function) block);

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
            position = new Vector2Int(position.x+250, position.y); //135 is the width of a start block
        }
        return blockToPosition;
    }
}
