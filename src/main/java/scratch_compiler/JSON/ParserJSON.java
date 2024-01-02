package scratch_compiler.JSON;

public class ParserJSON {
    public static ToJSON parse(String json) {
        return parse(new ReaderJSON(json));
    }

    private static ToJSON parse(ReaderJSON reader) {
        if (reader.startsWith("{"))
            return parseObject(reader);
        else if (reader.startsWith("["))
            return parseArray(reader);
        else if (reader.startsWith("\""))
            return parseString(reader);
        else if (reader.startsWith("true"))
            return parseBoolean(reader);
        else if (reader.startsWith("false"))
            return parseBoolean(reader);
        else if (reader.startsWith("null"))
        {
            reader.strip("null");
            return null;
        }
        else if (Character.isDigit(reader.peek().charAt(0)) || reader.peek().charAt(0) == '-')
            return parseNumber(reader);
        else
            throw new IllegalArgumentException("Invalid JSON");
    }

    private static ObjectJSON parseObject(ReaderJSON reader) {
        ObjectJSON object = new ObjectJSON();
        if (!reader.startsWith("{"))
            throw new IllegalArgumentException("Expected '{' at start of object");
        reader.strip("{");

        while (!reader.startsWith("}")&&!reader.isAtEnd()){
            String key = parseString(reader).getValue();
            if (!reader.startsWith(":"))
                throw new IllegalArgumentException("Expected ':' after key");
            reader.strip(":");
            ToJSON value = parse(reader);
            object.setValue(key, value);
            if (reader.startsWith(","))
                reader.strip(",");
            else if (!reader.startsWith("}"))
                throw new IllegalArgumentException("Expected ',' or '}' after value, got "+reader.peek());
        }

        if (!reader.startsWith("}"))
            throw new IllegalArgumentException("Expected '}' at end of object");
        reader.strip("}");
        return object;
    }

    private static StringJSON parseString(ReaderJSON reader)
    {
        if (!reader.startsWith("\""))
            throw new IllegalArgumentException("Expected '\"' at start of string");
        reader.strip("\"");
        String result="";
        while (!reader.startsWith("\""))
        {
            if (reader.startsWith("\\"))
            {
                reader.strip("\\");
                if (reader.startsWith("\"")){
                    result+="\"";
                    reader.strip("\"");
                }
                else if (reader.startsWith("\\")){
                    result+="\\";
                    reader.strip("\\");
                }
                else if (reader.startsWith("/")){
                    result+="/";
                    reader.strip("/");
                }
                else if (reader.startsWith("b")){
                    result+="\b";
                    reader.strip("b");
                }
                else if (reader.startsWith("f")){
                    result+="\f";
                    reader.strip("f");
                }
                else if (reader.startsWith("n")){
                    result+="\n";
                    reader.strip("n");
                }
                else if (reader.startsWith("r")){
                    result+="\r";
                    reader.strip("r");
                }
                else if (reader.startsWith("t")){
                    result+="\t";
                    reader.strip("t");
                }
                else if (reader.startsWith("u")){
                    reader.strip("u");
                    String hex=reader.read(4);
                    result+=(char)Integer.parseInt(hex,16);
                }
                else{
                    throw new IllegalArgumentException("Invalid escape character");
                }
            }
            else
            {
                result+=reader.read();
                reader.removeWhitespace();
            }
        }
        reader.strip("\"");
        return new StringJSON(result);
    }

    private static ArrayJSON parseArray(ReaderJSON reader)
    {
        ArrayJSON array = new ArrayJSON();
        if (!reader.startsWith("["))
            throw new IllegalArgumentException("Expected '[' at start of array");
        reader.strip("[");
        while (!reader.startsWith("]")&&!reader.isAtEnd())
        {
            array.addValue(parse(reader));
            if (reader.startsWith(","))
                reader.strip(",");
            else if (!reader.startsWith("]"))
                throw new IllegalArgumentException("Expected ',' or ']' after value");
        }
        if (!reader.startsWith("]"))
            throw new IllegalArgumentException("Expected ']' at end of array");
        reader.strip("]");
        return array;
    }

    private static NumberJSON parseNumber(ReaderJSON reader)
    {
        String number="";
        while (!reader.isAtEnd()&&(Character.isDigit(reader.peek().charAt(0))||reader.peek().charAt(0)=='.'||reader.peek().charAt(0)=='-'))
            number+=reader.read();
        
        reader.removeWhitespace();
        return new NumberJSON(Double.parseDouble(number));
    }

    private static BooleanJSON parseBoolean(ReaderJSON reader)
    {
        if (reader.startsWith("true"))
        {
            reader.strip("true");
            return new BooleanJSON(true);
        }
        else if (reader.startsWith("false"))
        {
            reader.strip("false");
            return new BooleanJSON(false);
        }
        else
            throw new IllegalArgumentException("Expected 'true' or 'false'");
    }


}
