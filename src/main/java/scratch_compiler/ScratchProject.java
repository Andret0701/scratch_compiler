package scratch_compiler;

public class ScratchProject {

    private String targetsToJSON() {
        String json = "\"targets\": [" +
                "        {\n" +
                "            \"isStage\": true,\n" +
                "            \"name\": \"Stage\",\n" +
                "            \"variables\": {\n" +
                "                \"`jEk@4|i[#Fk?(8x)AV.-my variable\": [\n" +
                "                    \"min variabel\",\n" +
                "                    0\n" +
                "                ]\n" +
                "            },\n" +
                "            \"lists\": {},\n" +
                "            \"broadcasts\": {},\n" +
                "            \"blocks\": {},\n" +
                "            \"comments\": {},\n" +
                "            \"currentCostume\": 0,\n" +
                "            \"costumes\": [\n" +
                "                {\n" +
                "                    \"assetId\": \"cd21514d0531fdffb22204e0ec5ed84a\",\n" +
                "                    \"name\": \"bakgrunn1\",\n" +
                "                    \"md5ext\": \"cd21514d0531fdffb22204e0ec5ed84a.svg\",\n" +
                "                    \"dataFormat\": \"svg\",\n" +
                "                    \"rotationCenterX\": 240,\n" +
                "                    \"rotationCenterY\": 180\n" +
                "                }\n" +
                "            ],\n" +
                "            \"sounds\": [\n" +
                "                {\n" +
                "                    \"assetId\": \"83a9787d4cb6f3b7632b4ddfebf74367\",\n" +
                "                    \"name\": \"plopp\",\n" +
                "                    \"dataFormat\": \"wav\",\n" +
                "                    \"format\": \"\",\n" +
                "                    \"rate\": 48000,\n" +
                "                    \"sampleCount\": 1123,\n" +
                "                    \"md5ext\": \"83a9787d4cb6f3b7632b4ddfebf74367.wav\"\n" +
                "                 }\n" +
                "            ],\n" +
                "            \"volume\": 100,\n" +
                "            \"layerOrder\": 0,\n" +
                "            \"tempo\": 60,\n" +
                "            \"videoTransparency\": 50,\n" +
                "            \"videoState\": \"on\",\n" +
                "            \"textToSpeechLanguage\": null\n" +
                "        },\n" +
                new Figure("Figur1").toJSON() +
                "    ]";
        return json;
    }

    public String toJSON() {
        String json = "{\n" +
                targetsToJSON() +
                ",\n" +
                "    \"monitors\": [],\n" +
                "    \"extensions\": [],\n" +
                "    \"meta\": {\n" +
                "        \"semver\": \"3.0.0\",\n" +
                "        \"vm\": \"0.2.0-prerelease.20220222132735\",\n" +
                "        \"agent\": \"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Scratch/3.29.1 Chrome/94.0.4606.81 Electron/15.3.1 Safari/537.36\"\n"
                +
                "    }\n" +
                "}";
        return json;
    }
}
