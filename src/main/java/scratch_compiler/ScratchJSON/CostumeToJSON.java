package scratch_compiler.ScratchJSON;

import java.util.ArrayList;

import scratch_compiler.Costume;
import scratch_compiler.JSON.ArrayJSON;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.Types.Vector2;

public class CostumeToJSON {
    public static ArrayJSON costumesToJSON(ArrayList<Costume> costumes, boolean isStage) {
        ArrayJSON costumesJSON = new ArrayJSON();
        for (Costume costume : costumes)
            costumesJSON.addObject(costumeToJSON(costume,isStage));

        return costumesJSON;
    }

    public static ObjectJSON costumeToJSON(Costume costume, boolean isStage) {
        ObjectJSON costumeJSON = AssetToJSON.assetToJSON(costume);

        if(!isStage)
            costumeJSON.setNumber("bitmapResolution", costume.getBitmapResolution());

        Vector2 center = costume.getCenter();
        costumeJSON.setNumber("rotationCenterX", center.x);
        costumeJSON.setNumber("rotationCenterY", center.y);
        return costumeJSON;
    }
}
