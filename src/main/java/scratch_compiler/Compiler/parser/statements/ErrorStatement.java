package scratch_compiler.Compiler.parser.statements;

public class ErrorStatement extends Statement {
    private String message;

    public ErrorStatement(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "*ERROR(" + message + ")";
    }
}
