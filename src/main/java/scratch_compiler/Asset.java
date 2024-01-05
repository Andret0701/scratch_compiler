package scratch_compiler;

import java.io.File;

public class Asset {
    public String path;
    public Asset(String path) {
        validatePath();
        this.path = path;
    }

    private void validatePath() {
        if (!new File(this.path).exists())
            throw new RuntimeException("Asset path does not exist");
    }

    public String getName() {
        return new File(this.path).getName();
    }

    public String getDataFormat() {
        String[] parts = this.getName().split("\\.");
        if (parts.length == 1)
            throw new RuntimeException("Asset has no data format");
        return parts[parts.length - 1];
    }

    public String getMd5ext() {
        return getName()+"."+getDataFormat();
    }

    public String getPath() {
        return path;
    }
}
