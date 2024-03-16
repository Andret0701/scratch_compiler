package scratch_compiler.Compiler.parser.expressions.values;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class StructValue extends Expression {
    private Type type;
    private ArrayList<Expression> fields;

    public StructValue(Type type, ArrayList<Expression> fields) {
        this.type = type;
        validateFields(fields);
        this.fields = fields;
    }

    private void validateFields(ArrayList<Expression> fields) {
        if (fields.size() != type.getFields().size())
            throw new RuntimeException("Invalid number of fields for struct " + type.getName());
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).getType().equals(type.getFields().get(i).getType()))
                throw new RuntimeException(
                        "Invalid type for field " + type.getFields().get(i).getName() + " in struct " + type.getName());
        }
    }

    public Type getType() {
        return type;
    }

    public ArrayList<Expression> getFields() {
        return new ArrayList<Expression>(fields);
    }

    @Override
    public String toString() {
        String out = type.getName() + "{";
        for (int i = 0; i < fields.size(); i++) {
            out += fields.get(i);
            if (i < fields.size() - 1)
                out += ", ";
        }
        out += "}";
        return out;
    }
}
