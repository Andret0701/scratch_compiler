package scratch_compiler.JSON;

public class StringJSON implements ToJSON{
    private String value;
    public StringJSON(String value)
    {
        this.value=value;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value=value;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof StringJSON)
            return ((StringJSON)other).getValue().equals(this.value);
        return false;
    }

    @Override
    public String toJSON()
    {
        return "\""+value+"\"";
    }
}