package scratch_compiler.Compiler.parser.expressions.values;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class ArrayValue extends Expression {
    private Type type;

    public ArrayValue(TypeDefinition type, ArrayList<Expression> values) {
        super(values.size());
        this.type = new Type(type, true);
        validateValues(values);
        for (int i = 0; i < values.size(); i++) {
            setExpression(i, values.get(i));
        }
    }

    private void validateValues(ArrayList<Expression> values) {
        for (Expression value : values) {
            if (!value.getType().getType().equals(type.getType()))
                throw new RuntimeException("Invalid type for array value");
        }
    }

    @Override
    public Type getType() {
        return type;
    }

    public ArrayList<Expression> getValues() {
        ArrayList<Expression> values = new ArrayList<>();
        for (int i = 0; i < getExpressionCount(); i++) {
            values.add(getExpression(i));
        }
        return values;
    }

    public int getSize() {
        return getExpressionCount();
    }

    @Override
    public String toString() {
        String out = "{";
        ArrayList<Expression> values = getValues();
        for (int i = 0; i < values.size(); i++) {
            out += values.get(i);
            if (i < values.size() - 1)
                out += ", ";
        }
        out += "}";
        return out;
    }

    public boolean isConstant() {

        for (Expression value : getValues()) {
            if (!value.isConstant())
                return false;
        }
        return true;
    }
}
