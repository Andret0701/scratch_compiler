package scratch_compiler.ScratchJSON;

import scratch_compiler.Field;

import java.util.ArrayList;

import scratch_compiler.Input;
import scratch_compiler.ScratchVariable;
import scratch_compiler.Blocks.FunctionCallBlock;
import scratch_compiler.Blocks.FunctionDefinitionBlock;
import scratch_compiler.Blocks.Types.Block;
import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.HatBlock;
import scratch_compiler.Blocks.Types.StackBlock;
import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.Types.Vector2;

public class BlockToJSON {
    public static ObjectJSON blocksToJSON(ArrayList<HatBlock> blocks) {
        ObjectJSON blocksJSON = new ObjectJSON();
        for (int i = 0; i < blocks.size(); i++) {
            Vector2 position = getBlockPosition(i);
            BlockJSON hatBlockJSON = hatblockToJSON(blocks.get(i), position);
            blocksJSON.add(hatBlockJSON.getBlocksJSON());
        }
        return blocksJSON;
    }

    private static BlockJSON hatblockToJSON(HatBlock hatBlock, Vector2 position) {
        BlockJSON hatBlockJSON;
        if (hatBlock instanceof FunctionDefinitionBlock)
            hatBlockJSON = FunctionToJSON.functionDefinitionToJSON((FunctionDefinitionBlock) hatBlock);
        else
            hatBlockJSON = blockToJSON(hatBlock);

        hatBlockJSON.getBlock().setNumber("x", position.x);
        hatBlockJSON.getBlock().setNumber("y", position.y);

        BlockJSON stackJSON = blockStackToJSON(hatBlock.getStack());
        if (stackJSON != null && stackJSON.getBlock() != null)
            hatBlockJSON = connectBlocks(hatBlockJSON, stackJSON);
        return hatBlockJSON;
    }

    private static BlockJSON blockStackToJSON(BlockStack blockStack) {
        if (blockStack.size() == 0)
            return new BlockJSON();

        // convert stack to JSON
        ArrayList<BlockJSON> stackJSON = new ArrayList<BlockJSON>();
        for (StackBlock stackBlock : blockStack) {
            BlockJSON blockJSON = blockToJSON(stackBlock);
            stackJSON.add(blockJSON);
        }

        // connect blocks
        while (stackJSON.size() > 1) {
            BlockJSON parentJSON = stackJSON.get(stackJSON.size() - 2);
            BlockJSON nextJSON = stackJSON.get(stackJSON.size() - 1);
            stackJSON.remove(stackJSON.size() - 1);
            stackJSON.remove(stackJSON.size() - 1);

            stackJSON.add(connectBlocks(parentJSON, nextJSON));
        }

        return stackJSON.get(0);
    }

    private static BlockJSON connectBlocks(BlockJSON parentJSON, BlockJSON nextJSON) {
        if (parentJSON == null || nextJSON == null)
            throw new IllegalArgumentException("parentJSON or nextJSON is null");

        String parentID = parentJSON.getBlockID();
        String nextID = nextJSON.getBlockID();

        if (parentJSON.getBlock().getValue("next") != null)
            throw new IllegalArgumentException("parentJSON already has a next block");

        if (nextJSON.getBlock().getValue("parent") != null)
            throw new IllegalArgumentException("nextJSON already has a parent block");

        // connect blocks
        parentJSON.getBlock().setString("next", nextID);
        nextJSON.getBlock().setString("parent", parentID);
        nextJSON.getBlock().setBoolean("topLevel", false);

        // add nextJSON to parentJSON
        ObjectJSON subBlocks = nextJSON.getSubBlocks();
        subBlocks.setObject(nextID, nextJSON.getBlock());
        parentJSON.addSubBlocks(subBlocks);

        return parentJSON;
    }

    public static BlockJSON blockToJSON(Block block) {
        if (block.getOpcode() == null)
            return null;

        ObjectJSON blockJSON = new ObjectJSON();
        blockJSON.setString("opcode", block.getOpcode());
        blockJSON.setObject("inputs", inputsToJSON(block));
        blockJSON.setObject("fields", fieldsToJSON(block));
        blockJSON.setValue("next", null);
        blockJSON.setValue("parent", null);
        blockJSON.setBoolean("shadow", false);
        blockJSON.setBoolean("topLevel", true);

        if (block instanceof FunctionCallBlock) {
            blockJSON.setObject("mutation",
                    FunctionToJSON.functionCallMutationToJSON(((FunctionCallBlock) block).getFunction()));
            blockJSON.setObject("inputs", FunctionToJSON.functionCallInputsToJSON(blockJSON.getObject("inputs"),
                    ((FunctionCallBlock) block).getFunction()));
        }

        return new BlockJSON(blockJSON, getBlockID(block), subBlocksToJSON(block));
    }

    private static ObjectJSON subBlocksToJSON(Block block) {
        ObjectJSON subBlocksJSON = new ObjectJSON();

        // adding inputblocks
        for (Input input : block.getInputs()) {
            Block valueBlock = input.getValueField();
            BlockJSON blockJSON = blockToJSON(valueBlock);
            if (blockJSON == null)
                continue;

            blockJSON.getBlock().setString("parent", getBlockID(block));
            blockJSON.getBlock().setBoolean("topLevel", false);

            subBlocksJSON.add(blockJSON.getBlocksJSON());
        }

        // adding substacks
        if (block instanceof StackBlock) {
            for (BlockStack substack : ((StackBlock) block).getSubstacks()) {
                if (substack == null || substack.size() == 0)
                    continue;

                BlockJSON stackJSON = blockStackToJSON(substack);

                stackJSON.getBlock().setString("parent", getBlockID(block));
                stackJSON.getBlock().setBoolean("topLevel", false);

                subBlocksJSON.add(stackJSON.getBlocksJSON());
            }
        }

        return subBlocksJSON;
    }

    private static ObjectJSON inputsToJSON(Block block) {
        ObjectJSON inputsJSON = new ObjectJSON();

        if (block instanceof StackBlock) {
            int stackNumber = 1;
            for (BlockStack substack : ((StackBlock) block).getSubstacks()) {
                if (substack == null || substack.size() == 0) {
                    stackNumber++;
                    continue;
                }

                String name = "SUBSTACK";
                if (stackNumber > 1)
                    name += stackNumber;

                ArrayJSON substackJSON = new ArrayJSON();
                substackJSON.addNumber(2);
                substackJSON.addString(getBlockID(substack.get(0)));
                inputsJSON.setArray(name, substackJSON);

                stackNumber++;
            }
        }

        for (Input input : block.getInputs())
            inputsJSON.setArray(input.getName(), ValueFieldToJSON.valueFieldToJSON(input.getValueField()));

        return inputsJSON;
    }

    private static ObjectJSON fieldsToJSON(Block block) {
        ObjectJSON fields = new ObjectJSON();
        for (Field field : block.getFields()) {
            ArrayJSON fieldJSON = new ArrayJSON();
            if (field.getVariable() != null) {
                ScratchVariable variable = field.getVariable();
                fieldJSON.addString(VariableToJSON.getVariableName(variable));
                fieldJSON.addString(VariableToJSON.getVariableId(variable));
                fields.setArray(field.getName(), fieldJSON);
            } else if (field.getType() != null) {
                fieldJSON.addString(field.getType());
                fieldJSON.addValue(null);
                fields.setArray(field.getName(), fieldJSON);
            }
        }

        return fields;
    }

    static String getBlockID(Block block) {
        return "id_block_" + block.getOpcode() + "_" + block.hashCode();
    }

    private static Vector2 getBlockPosition(int stackNumber) {
        return new Vector2(250 * stackNumber, 0);
    }
}
