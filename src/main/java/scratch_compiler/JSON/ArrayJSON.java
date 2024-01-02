package scratch_compiler.JSON;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrayJSON implements ToJSON, Iterable<ToJSON> {
    private ArrayList<ToJSON> array;

    public ArrayJSON() {
        array = new ArrayList<ToJSON>();
    }

    public void addValue(ToJSON value) {
        array.add(value);
    }

    public void addNumber(double value) {
        addValue(new NumberJSON(value));
    }

    public void addBoolean(boolean value) {
        addValue(new BooleanJSON(value));
    }

    public void addString(String value) {
        addValue(new StringJSON(value));
    }

    public void addObject(ObjectJSON value) {
        addValue(value);
    }

    public void addArray(ArrayJSON value) {
        addValue(value);
    }

    public ToJSON getValue(int index) {
        return array.get(index);
    }
    
    public double getNumber(int index) {
        ToJSON value = getValue(index);
        if (!(value instanceof NumberJSON))
            throw new RuntimeException("The value of the index " + index + " is not a number");
        return ((NumberJSON) value).getValue();
    }

    public boolean getBoolean(int index) {
        ToJSON value = getValue(index);
        if (!(value instanceof BooleanJSON))
            throw new RuntimeException("The value of the index " + index + " is not a boolean");
        return ((BooleanJSON) value).getValue();
    }

    public String getString(int index) {
        ToJSON value = getValue(index);
        if (!(value instanceof StringJSON))
            throw new RuntimeException("The value of the index " + index + " is not a string");
        return ((StringJSON) value).getValue();
    }

    public ObjectJSON getObject(int index) {
        ToJSON value = getValue(index);
        if (!(value instanceof ObjectJSON))
            throw new RuntimeException("The value of the index " + index + " is not an object");
        return (ObjectJSON) value;
    }

    public ArrayJSON getArray(int index) {
        ToJSON value = getValue(index);
        if (!(value instanceof ArrayJSON))
            throw new RuntimeException("The value of the index " + index + " is not an array");
        return (ArrayJSON) value;
    }

    public void remove(int index) {
        array.remove(index);
    }

    public void setValue(int index, ToJSON value) {
        array.set(index, value);
    }

    public void setNumber(int index, double value) {
        setValue(index, new NumberJSON(value));
    }

    public void setBoolean(int index, boolean value) {
        setValue(index, new BooleanJSON(value));
    }

    public void setString(int index, String value) {
        setValue(index, new StringJSON(value));
    }

    public void setObject(int index, ObjectJSON value) {
        setValue(index, value);
    }

    public void setArray(int index, ArrayJSON value) {
        setValue(index, value);
    }

    public int size() {
        return array.size();
    }

    @Override
    public String toJSON() {
        String json = "[";
        for (int i = 0; i < this.size(); i++) {
            ToJSON value = getValue(i);
            if (value == null)
                json += "null";
            else
                json += value.toJSON();

            if (i < this.size() - 1)
                json += ",";
        }

        json+="]";
        return json;
    }

    @Override
    public Iterator<ToJSON> iterator() {
        return array.iterator();
    }


}
