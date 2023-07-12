package scratch_compiler;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
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

            return zipFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
