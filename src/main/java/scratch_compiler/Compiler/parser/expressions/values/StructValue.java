package scratch_compiler.Compiler.parser.expressions.values;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.parser.expressions.Expression;

public class StructValue extends Expression {
    private TypeDefinition type;
    private ArrayList<Expression> fields;

    public StructValue(TypeDefinition type, ArrayList<Expression> fields) {
        this.type = type;
        validateFields(fields);
        this.fields = fields;
    }

    private void validateFields(ArrayList<Expression> fields) {
        if (fields.size() != type.getFields().size())
            throw new RuntimeException("Invalid number of fields for struct " + type.getName());

        ArrayList<String> fieldNames = type.getFields();
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).getType().getType().equals(type.getField(fieldNames.get(i))))
                throw new RuntimeException(
                        "Invalid type for field " + fieldNames.get(i) + " in struct " + type.getName());
        }
    }

    @Override
    public Type getType() {
        return new Type(type);
    }

    public Expression getField(String name) {
        ArrayList<String> fieldNames = type.getFields();
        for (int i = 0; i < fieldNames.size(); i++) {
            if (fieldNames.get(i).equals(name))
                return fields.get(i);
        }
        throw new RuntimeException("Field " + name + " not found in struct " + type.getName());
    }

    public ArrayList<Expression> getFields() {
        return new ArrayList<Expression>(fields);
    }

    @Override
    public String toString() {
        String out = "{";
        for (int i = 0; i < fields.size(); i++) {
            out += fields.get(i);
            if (i < fields.size() - 1)
                out += ", ";
        }
        out += "}";
        return out;
    }

    public boolean isConstant() {
        return true;
    }
}
