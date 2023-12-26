package scratch_compiler;

import scratch_compiler.JSON.NestedJSON;

public class Background extends NestedJSON{
    public Background() {
        super();
        setBoolean("isStage", true);
        setString("name", "Stage");
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
                "            \"layerOrder\": 0");
        setInteger("tempo", 60);
        setInteger("videoTransparency", 50);
        setString("videoState", "on");
        setString("textToSpeechLanguage", null);
        
    }
}
