package scratch_compiler.ScratchJSON;

import scratch_compiler.Asset;
import scratch_compiler.JSON.ObjectJSON;

public class AssetToJSON {
    public static ObjectJSON assetToJSON(Asset asset)
    {
        ObjectJSON assetJSON = new ObjectJSON();
        assetJSON.setString("assetId", getAssetId(asset));
        assetJSON.setString("name", asset.getName());
        assetJSON.setString("dataFormat", asset.getDataFormat());
        assetJSON.setString("md5ext", asset.getMd5ext());
        return assetJSON;
    }

    public static String getAssetId(Asset asset)
    {
        return asset.getName() + "_" + asset.getDataFormat();
    }
}
