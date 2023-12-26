package scratch_compiler.JSON;

public class IntegerJSON implements ToJSON{
    private int value;
    public IntegerJSON(int value)
    {
        this.value=value;
    }

    public int getValue()
    {
        return this.value;
    }

    public String toJSON()
    {
        return Integer.toString(value);
    }
}
