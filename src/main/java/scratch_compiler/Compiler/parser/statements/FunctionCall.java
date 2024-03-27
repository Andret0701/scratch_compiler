package scratch_compiler.Compiler.parser.statements;

import java.util.ArrayList;

import scratch_compiler.Compiler.Function;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class FunctionCall extends Statement {
    private Function function;
    private ArrayList<Expression> arguments;

    public FunctionCall(Function function, ArrayList<Expression> arguments) {
        this.function = function;
        this.arguments = new ArrayList<Expression>(arguments);
    }

    public Function getFunction() {
        return function;
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
        args = "(" + args + ")";
        return function.getName() + args;
    }

    public ArrayList<Statement> getStatements() {
        return new ArrayList<Statement>();
    }
}
