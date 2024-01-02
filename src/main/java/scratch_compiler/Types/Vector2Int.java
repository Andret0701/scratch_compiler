package scratch_compiler.Types;

public class Vector2Int {
    public int x,y;
    public Vector2Int(int x,int y)
    {
        this.x=x;
        this.y=y;
    }

    public Vector2Int(double x,double y)
    {
        this.x=(int)x;
        this.y=(int)y;
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Vector2Int))
            return false;
        Vector2Int vector=(Vector2Int) other;
        return vector.x==x&&vector.y==y;
    }
    

    public Vector2Int copy()
    {
        return new Vector2Int(x,y);
    }
}
