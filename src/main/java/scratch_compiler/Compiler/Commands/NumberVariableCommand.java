package scratch_compiler.Compiler.Commands;

public class NumberVariableCommand extends Command {
    public NumberVariableCommand(String code) {
        if (code.startsWith(getOpcode()))
            code = code.substring(getOpcode().length());

    }

    public String getOpcode() {
        return "Number";
    }
}
