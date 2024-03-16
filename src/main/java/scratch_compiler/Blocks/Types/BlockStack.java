package scratch_compiler.Blocks.Types;

import java.util.ArrayList;
import java.util.Iterator;

public class BlockStack implements Iterable<StackBlock> {
    private ArrayList<StackBlock> blocks;

    public BlockStack() {
        blocks = new ArrayList<>();
    }

    public BlockStack(StackBlock block) {
        this();
        push(block);
    }

    public void push(StackBlock block) {
        if (isFinished())
            throw new RuntimeException("BlockStack is finished");
        if (blocks.contains(block))
            throw new RuntimeException("BlockStack already contains block");

        blocks.add(block);
    }

    public StackBlock pop() {
        return blocks.remove(blocks.size() - 1);
    }

    public void push(BlockStack blockStack) {
        for (StackBlock block : blockStack.blocks)
            push(block);
    }

    public boolean isFinished() {
        if (blocks.size() == 0)
            return false;
        return blocks.get(blocks.size() - 1).isEnd();
    }

    @Override
    public Iterator<StackBlock> iterator() {
        return blocks.iterator();
    }

    public int size() {
        return blocks.size();
    }

    public StackBlock get(int index) {
        return blocks.get(index);
    }

    @Override
    public String toString() {
        return blocks.toString();
    }

}
