package scratch_compiler.JSON;

public class BooleanJSON implements ToJSON {
    private boolean value;
    public BooleanJSON(boolean value)
    {
        this.value=value;
    }

    public boolean getValue()
    {
        return this.value;
    }

    public void setValue(boolean value)
    {
        this.value=value;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof BooleanJSON)
            return ((BooleanJSON)other).getValue()==this.value;
        return false;
    }
    
    @Override
    public String toJSON()
    {
        return Boolean.toString(value);
    }
}
