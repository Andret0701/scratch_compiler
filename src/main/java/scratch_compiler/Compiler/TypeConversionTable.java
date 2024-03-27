package scratch_compiler.Compiler;

import java.util.ArrayList;
import java.util.HashMap;

import scratch_compiler.Compiler.parser.expressions.Expression;
import scratch_compiler.Compiler.parser.expressions.TypeConversionExpression;

public class TypeConversionTable {
    private HashMap<Type, ArrayList<Type>> table;

    public TypeConversionTable() {
        table = new HashMap<Type, ArrayList<Type>>();
    }

    public void addConversion(Type from, Type to) {
        if (from.equals(to))
            return;

        validateConversion(from, to);

        if (!table.containsKey(from))
            table.put(from, new ArrayList<Type>());

        table.get(from).add(to);
    }

    public boolean canConvert(Type from, Type to) {
        if (from.equals(to))
            return true;

        if (!table.containsKey(from))
            return false;

        for (Type t : table.get(from)) {
            if (canConvert(t, to))
                return true;
        }

        return false;
    }

    private ArrayList<Type> getConversions(Type from, Type to) {
        ArrayList<Type> conversions = new ArrayList<Type>();
        if (from.equals(to))
            return conversions;

        if (!table.containsKey(from))
            return null;

        for (Type t : table.get(from)) {
            if (canConvert(t, to)) {
                ArrayList<Type> temp = getConversions(t, to);
                if (temp != null) {
                    conversions.add(t);
                    conversions.addAll(temp);
                    return conversions;
                }
            }
        }

        return null;
    }

    public Expression convert(Expression expression, Type to) {
        if (expression.getType().equals(to))
            return expression;

        if (expression.getType().isArray() || !canConvert(expression.getType(), to))
            throw new RuntimeException("Invalid conversion from " + expression.getType() + " to " + to);

        ArrayList<Type> conversions = getConversions(expression.getType(), to);
        Expression converted = new TypeConversionExpression(expression, conversions.get(0));
        for (int i = 1; i < conversions.size(); i++) {
            converted = new TypeConversionExpression(converted, conversions.get(i));
        }

        return converted;
    }

    private void validateConversion(Type from, Type to) {
        // make sure that there is no circular conversion and that every conversion is
        // unique
        if (canConvert(to, from) && !from.equals(to))
            throw new RuntimeException("Invalid conversion from " + from + " to " + to);

        // add the rest later
    }

    public TypeConversionTable copy() {
        TypeConversionTable clone = new TypeConversionTable();
        for (Type from : table.keySet()) {
            for (Type to : table.get(from)) {
                clone.addConversion(from, to);
            }
        }

        return clone;
    }

    @Override
    public String toString() {
        String out = "";
        for (Type from : table.keySet()) {
            for (Type to : table.get(from)) {
                out += from + " -> " + to + "\n";
            }
        }
        return out;
    }
}
