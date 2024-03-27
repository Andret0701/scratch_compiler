package scratch_compiler.Compiler.parser.expressions;

import java.util.ArrayList;

import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.Type;

public class FunctionCallExpression extends Expression {
    private Function function;
    private ArrayList<Expression> arguments;

    public FunctionCallExpression(Function function, ArrayList<Expression> arguments) {
        super(arguments.size());
        for (int i = 0; i < arguments.size(); i++) {
            setExpression(i, arguments.get(i));
        }
        this.function = function;
    }

    @Override
    public Type getType() {
        return function.getReturnType();
    }

    public Function geFunction() {
        return function;
    }

    public ArrayList<Expression> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        String out = function.getName() + "(";
        for (int i = 0; i < expressions.size(); i++) {
            out += expressions.get(i).toString();
            if (i < expressions.size() - 1)
                out += ", ";
        }
        out += ")";
        return out;
    }

}
