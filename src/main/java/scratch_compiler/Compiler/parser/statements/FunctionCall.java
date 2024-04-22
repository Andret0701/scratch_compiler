package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class FunctionCall extends Statement {
    private Function function;

    public FunctionCall(Function function, ArrayList<Expression> arguments) {
        super(arguments.size());
        this.function = function;
        for (int i = 0; i < arguments.size(); i++)
            setExpression(i, arguments.get(i));

    }

    public Function getFunction() {
        return function;
    }

    public ArrayList<Expression> getArguments() {
        ArrayList<Expression> arguments = new ArrayList<>();
        for (int i = 0; i < getExpressionCount(); i++)
            arguments.add(getExpression(i));
        return arguments;
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
        args = "(" + args + ")";
        return function.getName() + args;
    }

}
