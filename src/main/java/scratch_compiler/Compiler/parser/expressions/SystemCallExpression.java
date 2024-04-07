package scratch_compiler.Compiler.parser.expressions;

import java.util.ArrayList;

import scratch_compiler.Compiler.SystemCall;
import scratch_compiler.Compiler.Type;

public class SystemCallExpression extends Expression {
    private SystemCall systemCall;

    public SystemCallExpression(SystemCall systemCall, ArrayList<Expression> arguments) {
        super(arguments.size());
        this.systemCall = systemCall;

        for (int i = 0; i < arguments.size(); i++) {
            setExpression(i, arguments.get(i));
        }
    }

    public SystemCall getSystemCall() {
        return systemCall;
    }

    public ArrayList<Expression> getArguments() {
        ArrayList<Expression> arguments = new ArrayList<>();
        for (int i = 0; i < getExpressionCount(); i++) {
            arguments.add(getExpression(i));
        }
        return arguments;
    }

    @Override
    public Type getType() {
        return systemCall.getReturnType();
    }

    @Override
    public String toString() {
        String args = "";
        ArrayList<Expression> arguments = getArguments();
        for (int i = 0; i < arguments.size(); i++) {
            args += arguments.get(i).toString();
            if (i < arguments.size() - 1)
                args += ", ";
        }
        args = "*(" + args + ")";
        return systemCall.getName() + args;
    }

}