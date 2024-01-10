package scratch_compiler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipOutputStream;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;

public class ZipUtils {
        public static void writeZipFile(String zipPath, String filename, String content) throws IOException {
            InputStream stream= new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            ZipParameters parameters=new ZipParameters();
            parameters.setFileNameInZip(filename);
            ZipFile zipFile =new ZipFile(zipPath);
            zipFile.addStream(stream,parameters);
            zipFile.close();
    }

    public static void writeZipFile(String zipPath, String filename, File content) throws IOException {
        ZipParameters parameters=new ZipParameters();
        parameters.setFileNameInZip(filename);
        ZipFile zipFile =new ZipFile(zipPath);
        zipFile.addFile(content,parameters);
        zipFile.close();
    }


    public static String readZipFile(String zipPath, String filename) throws ZipException, IOException {
        ZipFile zipFile = new ZipFile(zipPath);
        FileHeader fileHeader = zipFile.getFileHeader(filename);
        if (fileHeader == null)
        {
            zipFile.close();
            throw new RuntimeException("File not found in zip: "+filename);
        }
        InputStream inputStream = zipFile.getInputStream(fileHeader);
        String content = new String(inputStream.readAllBytes());
        zipFile.close();
        return content;
    }


    public static void createZipFolder(String path)
    {
        try{
        File zipFolder = new File(path);
        if(zipFolder.exists())
            zipFolder.delete();
        if(!zipFolder.exists())
        {
            zipFolder.createNewFile();
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFolder));
            zos.close();
        }
        }catch(Exception e)
            {
                e.printStackTrace();
            }
    }
}
