package scratch_compiler.JSON;

public class ReaderJSON {
    private String json;
    private int index = 0;
    public ReaderJSON(String json) {
        this.json = json.strip();
    }

    public String peek(int offset) {
        int i=index+offset;
        if (i>=json.length())
            return "";
        return json.substring(i,i+1);        
    }

    public String peek() {
        return peek(0);
    }

    public String read(int length) {
        String result = json.substring(index, index + length);
        index += length;
        return result;
    }

    public String read() {
        return read(1);
    }

    public boolean startsWith(String str) {
        if (isAtEnd())
            return false;
        return json.startsWith(str, index);
    }

    public boolean isAtEnd() {
        return index >= json.length();
    }

    public void strip(String str)
    {
        if (!startsWith(str))
            return;
        index+=str.length();
        removeWhitespace();
    }

    public void removeWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peek().charAt(0)))
            read();
    }

}
