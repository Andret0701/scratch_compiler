package scratch_compiler.Types;

public class Vector2 {
    public double x;
    public double y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    
    public Vector2 copy() {
        return new Vector2(this.x, this.y);
    }
}
