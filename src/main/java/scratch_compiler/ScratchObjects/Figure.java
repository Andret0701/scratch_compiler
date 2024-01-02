package scratch_compiler.ScratchObjects;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Blocks.LoopBlock;
import scratch_compiler.Blocks.LoopForeverBlock;
import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.SetNumberVariableBlock;
import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.Blocks.BlockTypes.HatBlock;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.JSON.StringJSON;
import scratch_compiler.JSON.ToJSON;
import scratch_compiler.Types.Vector2Int;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.ModulusField;
import scratch_compiler.ValueFields.MultiplicationField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.NumberVariableField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.Variables.Variable;
import scratch_compiler.Variables.VariableType;

public class Figure extends ScratchObject {
    private Vector2Int position = new Vector2Int(0, 0);
    private int size=100;
    private int direction=90;
    private boolean visible = true;
    public Figure(String name) {
        super(name);


        /*
        NumberVariable test = new NumberVariable("test variabel", 0);
        variables.add(test);
        Block motion = new Block("motion_ifonedgebounce");
        Block motion2 = new Block("motion_ifonedgebounce");
        Block flag = new Block("event_whenflagclicked");

        Block move = new MoveBlock(new SubtractionField(
                new AdditionField(
                        new MultiplicationField(
                                new DivisionField(new ModulusField(new NumberVariableField(test), new NumberField(1)),
                                        new NumberField(1)),
                                new NumberField(1)),
                        new NumberField(1)),
                new NumberField(10)));
        motion.connectTo(flag);
        motion2.connectTo(flag);

        move.connectTo(motion2);

        blocks.add(flag);
        blocks.add(motion2);
        blocks.add(motion);
        blocks.add(move);

        Block flag2 = new Block("event_whenflagclicked");
        ContainerBlock loop = new LoopForeverBlock();

        loop.connectTo(flag2);

        Block move2 = new MoveBlock(new NumberField(10));
        move2.connectInside(loop);

        blocks.add(flag2);
        blocks.add(loop);
        blocks.add(move2);

        Block flag3 = new Block("event_whenflagclicked");
        ContainerBlock loop2 = new LoopBlock(new NumberField(10));
        Block move3 = new MoveBlock(new NumberField(10));

        loop2.connectTo(flag3);
        move3.connectInside(loop2);

        blocks.add(flag3);
        blocks.add(loop2);
        blocks.add(move3);

        Block setVariable = new SetNumberVariableBlock(test, new NumberField(10));
        setVariable.connectTo(loop2);

        blocks.add(setVariable);*/
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
        for (Variable variable : getVariables())
            clone.addVariable(variable.clone());

        for (Block block : getBlocks())
            clone.addBlock(block.clone());
        return clone;
    }
}
