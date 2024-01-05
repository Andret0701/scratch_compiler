package scratch_compiler.ScratchJSON;

import scratch_compiler.Costume;
import scratch_compiler.JSON.ObjectJSON;
import scratch_compiler.Types.Vector2;

public class CostumeToJSON {
    public static ObjectJSON costumeToJSON(Costume costume) {
        ObjectJSON costumeJSON = AssetToJSON.assetToJSON(costume);
        costumeJSON.setNumber("bitmapResolution", costume.getBitmapResolution());

        Vector2 center = costume.getCenter();
        costumeJSON.setNumber("rotationCenterX", center.x);
        costumeJSON.setNumber("rotationCenterY", center.y);
        return costumeJSON;
    }
}
