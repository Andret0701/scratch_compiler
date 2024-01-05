package scratch_compiler;

import scratch_compiler.Types.Vector2;

public class Costume extends Asset {
    private Vector2 center;    
    public Costume(String path, Vector2 center)
    {
        super(path);
        validateDataFormat(getDataFormat());    
        this.center = center.copy();
    }    

    private void validateDataFormat(String dataFormat) {
        if (!dataFormat.equals("svg") && !dataFormat.equals("png") && !dataFormat.equals("jpg")) {
            throw new IllegalArgumentException("Invalid data format: " + dataFormat);
        }
    }

    public Vector2 getCenter() {
        return center.copy();
    }


    public int getBitmapResolution() {
        return 1;
    }


    public Costume clone() {
        return new Costume(getPath(), center);
    }
}
