package scratch_compiler.Compiler.parser.expressions;

import java.util.ArrayList;
import scratch_compiler.Compiler.Type;

public class FunctionCallExpression extends OperatorExpression {
    private String functionName;
    private ArrayList<Expression> arguments;

    public FunctionCallExpression(String functionName, ArrayList<Expression> arguments, Type returnType) {
        super(returnType);
        addAllOperands(arguments);
        this.functionName = functionName;
    }

    @Override
    public String getOperator() {
        return functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public ArrayList<Expression> getArguments() {
        return arguments;
    }

}
