package scratch_compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import scratch_compiler.ScratchJSON.ScratchProjectToJSON;

public class ScratchProjectCreater {
    public static void createProject(String name,String path, ScratchProject project) throws IOException {
        String filePath = path+"\\"+name+".sb3";
        ZipUtils.createZipFolder(filePath);
        ZipUtils.writeZipFile(filePath, "project.json", ScratchProjectToJSON.projectToJSON(project).toJSON());
        ArrayList<Asset> assets = project.getAssets();
        for (Asset asset : assets) 
        {
            File file = new File(asset.getPath());
            if(!file.exists())
                throw new RuntimeException("Asset path does not exist: "+asset.getPath());
            ZipUtils.writeZipFile(filePath, asset.getMd5ext(), file);
        }        
    } 
}
