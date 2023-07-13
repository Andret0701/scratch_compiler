package scratch_compiler;

import java.util.ArrayList;

public class Figure {
    private String name;
    ArrayList<Block> blocks = new ArrayList<Block>();
    ArrayList<Variable> variables = new ArrayList<Variable>();

    int x, y;

    public Figure(String name) {
        this.name = name;
        x = 48;
        y = 50;

        variables.add(new Variable("test variabel", "0"));
        Block motion = new Block("motion_ifonedgebounce");
        Block motion2 = new Block("motion_ifonedgebounce");
        Block flag = new Block("event_whenflagclicked");

        motion.setParent(flag);
        motion2.setParent(flag);

        blocks.add(flag);
        blocks.add(motion2);
        blocks.add(motion);
    }

    private String variablesToJSON() {
        String json = "\"variables\": {";

        for (int i = 0; i < variables.size(); i++) {
            Variable variable = variables.get(i);
            json += variable.toJSON();
            if (i < variables.size() - 1)
                json += ",";
        }
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

    public String toJSON() {
        String json = "{";
        json += "\"isStage\": false,";
        json += "\"name\": \"" + name + "\",";

        json += variablesToJSON() + ",";
        json += "\"lists\": {},";
        json += "\"broadcasts\": {},";
        json += blocksToJSON() + ",";
        json += "\"comments\": {}," +
                "\"currentCostume\": 0,\n" +
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
                "            \"visible\": true,\n" +

                "\"x\": " + x + "," +
                "\"y\": " + y + "," +

                "            \"size\": 100,\n" +
                "            \"direction\": 90,\n" +
                "            \"draggable\": false,\n" +
                "            \"rotationStyle\": \"all around\"\n" +
                "        }";

        return json;
    }
}
