package scratch_compiler;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Blocks.LoopBlock;
import scratch_compiler.Blocks.LoopForeverBlock;
import scratch_compiler.Blocks.MoveBlock;
import scratch_compiler.Blocks.SetNumberVariableBlock;
import scratch_compiler.Blocks.BlockTypes.Block;
import scratch_compiler.Blocks.BlockTypes.HatBlock;
import scratch_compiler.JSON.NestedJSON;
import scratch_compiler.JSON.ToJSON;
import scratch_compiler.Types.Vector2Int;
import scratch_compiler.ValueFields.AdditionField;
import scratch_compiler.ValueFields.DivisionField;
import scratch_compiler.ValueFields.ModulusField;
import scratch_compiler.ValueFields.MultiplicationField;
import scratch_compiler.ValueFields.NumberField;
import scratch_compiler.ValueFields.NumberVariableField;
import scratch_compiler.ValueFields.SubtractionField;
import scratch_compiler.Variables.NumberVariable;
import scratch_compiler.Variables.Variable;
import scratch_compiler.Variables.VariableType;

public class Figure extends NestedJSON {
    private ArrayList<HatBlock> blocks = new ArrayList<HatBlock>();
    private HashMap<String, VariableType> variables = new HashMap<String, VariableType>();
    public Figure(String name) {
        super();
        setBoolean("isStage", false);
        setString("name", name);
        setNested("variables", new NestedJSON());
        setNested("lists", new NestedJSON());
        setNested("broadcasts", new NestedJSON());
        setNested("blocks", new NestedJSON());
        setNested("comments", new NestedJSON());
        setString("currentCostume",  "§0,\n" +
                "            \"costumes\": [\n" +
                "                {\n" +
                "                    \"assetId\": \"bcf454acf82e4504149f7ffe07081dbc\",\n" +
                "                    \"name\": \"drakt1\",\n" +
                "                    \"bitmapResolution\": 1,\n" +
                "                    \"md5ext\": \"bcf454acf82e4504149f7ffe07081dbc.svg\",\n" +
                "                    \"dataFormat\": \"svg\",\n" +
                "                    \"rotationCenterX\": 48,\n" +
                "                    \"rotationCenterY\": 50\n" +
                "                },\n" +
                "                {\n" +
                "                    \"assetId\": \"0fb9be3e8397c983338cb71dc84d0b25\",\n" +
                "                    \"name\": \"drakt2\",\n" +
                "                    \"bitmapResolution\": 1,\n" +
                "                    \"md5ext\": \"0fb9be3e8397c983338cb71dc84d0b25.svg\",\n" +
                "                    \"dataFormat\": \"svg\",\n" +
                "                    \"rotationCenterX\": 46,\n" +
                "                    \"rotationCenterY\": 53\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sounds\": [\n" +
                "                {\n" +
                "                    \"assetId\": \"83c36d806dc92327b9e7049a565c6bff\",\n" +
                "                    \"name\": \"Mjau\",\n" +
                "                    \"dataFormat\": \"wav\",\n" +
                "                    \"format\": \"\",\n" +
                "                    \"rate\": 48000,\n" +
                "                    \"sampleCount\": 40681,\n" +
                "                    \"md5ext\": \"83c36d806dc92327b9e7049a565c6bff.wav\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"volume\": 100,\n" +
                "            \"layerOrder\": 1,\n" +
                "            \"visible\": true");

        setInteger("x",0);
        setInteger("y",0);
        setInteger("size",100);
        setInteger("direction",10);
        setBoolean("draggable", false);
        setString("rotationStyle", "all around");

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

    public void setName(String name)
    {
        setString("name", name);
    }

    public String getName()
    {
        return getString("name");
    }

    public void setPosition(int x, int y)
    {
        setInteger("x", x);
        setInteger("y", y);
    }

    public void setPosition(Vector2Int position)
    {
        setPosition(position.x,position.y);
    }

    public Vector2Int getPosition()
    {
        return new Vector2Int(getInteger("x"),getInteger("y"));
    }

    public void setSize(int size)
    {
        setInteger("size", size);
    }

    public int getSize()
    {
        return getInteger("size");
    }

    public void setDirection(int direction)
    {
        setInteger("direction", direction);
    }

    public int getDirection()
    {
        return getInteger("direction");
    }

    

    public void addBlock(HatBlock block) {
       // blocks.add(block.clone());
    }

    private boolean containsVariable(String name) {
        return true;//variables.containsKey(name);
    }

    public void addVariable(String name, VariableType type) {
        if (containsVariable(name))
            throw new IllegalArgumentException("Variable with name " + name + " already exists");
        
        //variables.put(name, type);
    }

    private String variablesToJSON() {
        String json = "\"variables\": {";

        int i = 0;
        //for (String name : variables.keySet()) {
           // VariableType type = variables.get(name);
           // json += "\"" + Utils.generateID() + "\": [\"" + name + "\", \"" + 0+ "\"]";
         //   if (i < variables.size() - 1)
            //    json += ",";
        //    i++;
        //}
        json += "}";
        return json;
    }

    private String blocksToJSON() {
        String json = "\"blocks\": {";

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            json += block.toJSON();
            if (i < blocks.size() - 1)
                json += ",";
        }
        json += "}";
        return json;
    }

    //copy 
    @Override
    public Figure clone() {
        Figure clone = new Figure(getName());
        clone.setPosition(getPosition());
        clone.setSize(getSize());
        clone.setDirection(getDirection());

        clone.blocks = new ArrayList<HatBlock>();
        //clone.variables = new HashMap<String, VariableType>();

        for (HatBlock block : blocks) {
            //clone.blocks.add(block.clone());
        }

       // for (String name : variables.keySet()) {
       //     VariableType type = variables.get(name);
       //     clone.variables.put(name, type);
       // }

        return clone;
    }
}
