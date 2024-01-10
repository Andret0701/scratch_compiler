package scratch_compiler.JSON;

public class StringJSON implements ToJSON{
    private String value;
    public StringJSON(String value)
    {
        setValue(value);
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
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
        String json = "\"";
        for (int i = 0; i < value.length(); i++)
        {
            char c = value.charAt(i);
            if (c == '"')
                json += "\\\"";
            else if (c == '\\')
                json += "\\\\";
            else if (c == '\b')
                json += "\\b";
            else if (c == '\f')
                json += "\\f";
            else if (c == '\n')
                json += "\\n";
            else if (c == '\r')
                json += "\\r";
            else if (c == '\t')
                json += "\\t";
            else
                json += c;
        }
        json += "\"";
        return json;
    }
}