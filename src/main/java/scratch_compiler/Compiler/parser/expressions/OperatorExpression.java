package scratch_compiler.Compiler.parser.expressions;

import java.util.ArrayList;
import scratch_compiler.Compiler.Type;

public abstract class OperatorExpression extends Expression {
    private ArrayList<Expression> operands;
    private Type returnType;

    public OperatorExpression(Type returnType) {
        this.returnType = returnType;
        this.operands = new ArrayList<>();
    }

    public abstract String getOperator();

    protected void addOperand(Expression operand) {
        operands.add(operand);
    }

    protected void addAllOperands(ArrayList<Expression> operands) {
        this.operands.addAll(operands);
    }

    protected Expression getOperand(int index) {
        if (index < 0 || index >= operands.size()
                || operands.get(index) == null)
            return null;
        return operands.get(index);
    }

    protected ArrayList<Expression> getOperands() {
        return new ArrayList<>(operands);
    }

    @Override
    public Type getType() {
        return returnType;
    }

    @Override
    public String toString() {
        String out = getOperator() + "(";
        for (int i = 0; i < operands.size(); i++) {
            out += operands.get(i).toString();
            if (i < operands.size() - 1)
                out += ", ";
        }
        out += ")";
        return out;
    }
}
