package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.SystemCall;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class SystemCallStatement extends Statement {
    private SystemCall systemCall;
    private ArrayList<Expression> arguments;

    public SystemCallStatement(SystemCall systemCall, ArrayList<Expression> arguments) {
        this.systemCall = systemCall;
        this.arguments = new ArrayList<Expression>(arguments);
    }

    public SystemCall getSystemCall() {
        return systemCall;
    }

    public ArrayList<Expression> getArguments() {
        return new ArrayList<Expression>(arguments);
    }

    @Override
    public String toString() {
        String args = "";
        for (int i = 0; i < this.arguments.size(); i++) {
            args += this.arguments.get(i).toString();
            if (i < this.arguments.size() - 1)
                args += ", ";
        }
        args = "*(" + args + ")";
        return systemCall.getName() + args;
    }

    public ArrayList<Statement> getStatements() {
        return new ArrayList<Statement>();
    }
}