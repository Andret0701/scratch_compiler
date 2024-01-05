package scratch_compiler.Compiler.parser.expressions;


import java.util.ArrayList;
import scratch_compiler.Compiler.parser.VariableType;

public abstract class OperationExpression extends Expression {
    private int precedence;
    private ArrayList<VariableType> definedFor = new ArrayList<>();
    public OperationExpression(int precedence) {
        this.precedence = precedence;       
        
    }

    protected void addDefinedFor(VariableType type)
    {
        if (!definedFor.contains(type))
            definedFor.add(type);
    }

    protected boolean isDefinedFor(VariableType type)
    {
        if (type==VariableType.STRUCT)
            return false;
        if (definedFor.contains(type))
            return true;
        if (type==VariableType.STRING)
            return false;
        return isDefinedFor(type.next());
    }

    protected VariableType getOutputType(VariableType operand)
    {
        if (!isDefinedFor(operand))
            throw new RuntimeException("Operation not supported for " + operand);

        if (definedFor.contains(operand))
            return operand;
    
        return getOutputType(operand.next());
    }

    public abstract String getOperator();
    public int getPrecedence() {
        return precedence;
    }
}
