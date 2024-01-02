package scratch_compiler.JSON;

public class RemoveLaterJSON  implements ToJSON{
    private String value;
    public RemoveLaterJSON(String value)
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
        if (other instanceof RemoveLaterJSON)
            return ((StringJSON)other).getValue().equals(this.value);
        return false;
    }

    @Override
    public String toJSON()
    {
        return value;
    }
}