package scratch_compiler;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static void writeZipFile(String zipPath, String filename, String content) {
        try {
            File zipFile = new File(zipPath);
            File tempFile = File.createTempFile("temp", ".zip");
            tempFile.deleteOnExit();

            // Read the existing zip file
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempFile));
            ZipEntry entry = zis.getNextEntry();

            String newFilePath = "";

            // Copy all entries except the one we want to replace
            while (entry != null) {
                String entryName = entry.getName();
                if (!entryName.endsWith(filename)) {
                    zos.putNextEntry(entry);
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                } else
                    newFilePath = entryName;
                entry = zis.getNextEntry();
            }

            // Add the new file
            ZipEntry newEntry = new ZipEntry(newFilePath);
            zos.putNextEntry(newEntry);
            zos.write(content.getBytes());
            zos.closeEntry();

            // Close streams
            zis.close();
            zos.close();

            // Replace the original zip file with the modified one
            if (zipFile.delete()) {
                if (!tempFile.renameTo(zipFile)) {
                    throw new IOException("Failed to rename temporary file to the original name");
                }
            } else {
                throw new IOException("Failed to delete the original zip file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readZipFile(String path, String filename) {
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(new File(path)));

            // read project.json
            String zipFile = "";

            ZipEntry entry = zip.getNextEntry();
            byte[] buffer = new byte[1024];
            int len;
            while (entry != null) {
                String name = entry.getName();
                if (name.endsWith(filename)) {
                    while ((len = zip.read(buffer)) > 0)
                        zipFile += new String(buffer, 0, len);

                }

                entry = zip.getNextEntry();
            }

            zip.closeEntry();
            return zipFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
