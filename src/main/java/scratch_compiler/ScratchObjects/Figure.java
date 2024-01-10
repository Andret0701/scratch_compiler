package scratch_compiler.ScratchObjects;

import scratch_compiler.Costume;
import scratch_compiler.Blocks.Block;
import scratch_compiler.Types.Vector2Int;

public class Figure extends ScratchObject {
    private Vector2Int position = new Vector2Int(0, 0);
    private int size=100;
    private int direction=90;
    private boolean visible = true;
    public Figure(String name) {
        super(name);
    }

    public void setPosition(int x, int y)
    {
        this.position = new Vector2Int(x, y);
    }

    public void setPosition(Vector2Int position)
    {
        setPosition(position.x,position.y);
    }

    public Vector2Int getPosition()
    {
        return this.position.copy();
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getSize()
    {
        return this.size;
    }

    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    public int getDirection()
    {
        return this.direction;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isVisible()
    {
        return this.visible;
    }


    //copy 
    @Override
    public Figure clone() {
        Figure clone = new Figure(getName());
        clone.setPosition(getPosition());
        clone.setSize(getSize());
        clone.setDirection(getDirection());
        clone.setVisible(isVisible());

        for(Costume costume : getCostumes())
            clone.addCostume(costume.clone());

        for (Block block : getBlocks())
            clone.addBlock(block.clone());
        return clone;
    }
}
