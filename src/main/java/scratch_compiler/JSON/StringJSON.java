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

    public String toJSON()
    {
        return "\""+value+"\"";
    }
}