package scratch_compiler.Types;

public class Vector2Int {
    public int x,y;
    public Vector2Int(int x,int y)
    {
        this.x=x;
        this.y=y;
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Vector2Int))
            return false;
        Vector2Int vector=(Vector2Int) other;
        return vector.x==x&&vector.y==y;
    }
    
}
