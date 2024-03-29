package scratch_compiler.Compiler.parser.expressions.values;

import java.util.ArrayList;

import scratch_compiler.Compiler.Type;
import scratch_compiler.Compiler.TypeDefinition;
import scratch_compiler.Compiler.TypeField;
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

        ArrayList<TypeField> typeFields = type.getFields();
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).getType().getType().equals(typeFields.get(i).getType()))
                throw new RuntimeException(
                        "Invalid type for field " + typeFields.get(i).getName() + " in struct " + type.getName());
        }
    }

    @Override
    public Type getType() {
        return new Type(type);
    }

    public Expression getField(String name) {
        ArrayList<TypeField> typeFields = type.getFields();
        for (int i = 0; i < typeFields.size(); i++) {
            if (typeFields.get(i).getName().equals(name))
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
