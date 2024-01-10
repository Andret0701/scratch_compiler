package scratch_compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5Checksum(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
        
        byte[] buffer = new byte[1024];
        int read;

        while ((read = fis.read(buffer)) != -1) {
            md.update(buffer, 0, read);
        }

        fis.close();

        byte[] md5sum = md.digest();
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < md5sum.length; i++) {
            String hex = Integer.toHexString(0xff & md5sum[i]);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}
