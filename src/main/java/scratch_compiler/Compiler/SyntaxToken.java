package scratch_compiler.Compiler;

import java.util.ArrayList;

public abstract class SyntaxToken {
    public int position;
    public String text;
    public Object value;

    public SyntaxToken(int position, String text, Object value) {
        this.position = position;
        this.text = text;
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public abstract String getOpcode();

    public abstract boolean isNextToken(Code code);

    public void stripToken(Code code) {
    }

    public abstract ArrayList<SyntaxToken> parseSubTokens(Code code);

    @Override
    public String toString() {
        return getOpcode() + ": '" + text + "'" + (value == null ? "" : " (" + value + ")");
    }
}
