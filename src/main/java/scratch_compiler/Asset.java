package scratch_compiler;

import java.io.File;

public class Asset {
    public String path;
    private String md5ext;
    public Asset(String path) {
        validatePath(path);
        this.path = path;


        File file = new File(path);
        try {
            md5ext = MD5.getMD5Checksum(file) + "." + getDataFormat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validatePath(String path) {
        if (!new File(path).exists())
            throw new RuntimeException("Asset path does not exist");
    }

    public String getName() {
        return new File(this.path).getName().split("\\.")[0];
    }

    public String getDataFormat() {
        String[] parts = this.path.split("\\.");
        if (parts.length == 1)
            throw new RuntimeException("Asset has no data format");
        return parts[parts.length - 1];
    }

    public String getMd5ext() {
        return md5ext;
    }

    public String getPath() {
        return path;
    }
}
