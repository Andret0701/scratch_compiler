package scratch_compiler.Compiler.parser.expressions.values;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class ArrayValue extends Expression {
    private Type type;
    private ArrayList<Expression> values;

    public ArrayValue(TypeDefinition type, ArrayList<Expression> values) {
        this.type = new Type(type, true);
        validateValues(values);
        this.values = new ArrayList<Expression>(values);
    }

    private void validateValues(ArrayList<Expression> values) {
        for (Expression value : values) {
            if (!value.getType().equals(type))
                throw new RuntimeException("Invalid type for array value");
        }
    }

    @Override
    public Type getType() {
        return type;
    }

    public ArrayList<Expression> getValues() {
        return new ArrayList<Expression>(values);
    }

    public int getSize() {
        return values.size();
    }

    @Override
    public String toString() {
        String out = "{";
        for (int i = 0; i < values.size(); i++) {
            out += values.get(i);
            if (i < values.size() - 1)
                out += ", ";
        }
        out += "}";
        return out;
    }

    public boolean isConstant() {
        for (Expression value : values) {
            if (!value.isConstant())
                return false;
        }
        return true;
    }
}
