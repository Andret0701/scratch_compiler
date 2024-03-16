package scratch_compiler;

import java.util.ArrayList;

import scratch_compiler.Blocks.Types.BlockStack;
import scratch_compiler.Blocks.Types.HatBlock;

public class ScratchProgram {
    private BlockStack stack;
    private ArrayList<HatBlock> hatBlocks;

    public ScratchProgram(BlockStack stack, ArrayList<HatBlock> hatBlocks) {
        this.stack = stack;
        this.hatBlocks = new ArrayList<>(hatBlocks);
    }

    public BlockStack getStack() {
        return stack;
    }

    public ArrayList<HatBlock> getHatBlocks() {
        return new ArrayList<>(hatBlocks);
    }
}
