package scratch_compiler.Compiler.Commands;

public abstract class Command {
    public Command() {
    }

    public abstract String getOpcode();

    public abstract boolean isNextCommand(Code code);

    public void stripToken(Code code) {
    }

    public abstract ArrayList<SyntaxToken> parseSubTokens(Code code);

    @Override
    public String toString() {
        return getOpcode() + ": '" + text + "'" + (value == null ? "" : " (" + value + ")");
    }
}
