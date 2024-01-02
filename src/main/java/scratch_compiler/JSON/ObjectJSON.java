package scratch_compiler.JSON;

import java.util.ArrayList;
import java.util.HashMap;

public class ObjectJSON implements ToJSON {
    private HashMap<String, ToJSON> values;
    public ObjectJSON() {
        values = new HashMap<>();
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public void remove(String key) {
        if (contains(key))
            values.remove(key);
    }

    public void setValue(String key, ToJSON value) {
        remove(key);
        values.put(key, value);
    }

    public void setNumber(String key, double value) {
        setValue(key, new NumberJSON(value));
    }

    public void setBoolean(String key, boolean value) {
        setValue(key, new BooleanJSON(value));
    }

    public void setString(String key, String value) {
        setValue(key, new StringJSON(value));
    }

    public void setObject(String key, ObjectJSON value) {
        setValue(key, value);
    }

    public void setArray(String key, ArrayJSON value) {
        setValue(key, value);
    }

    public ToJSON getValue(String key) {
        if (contains(key))
            return values.get(key);
        return null;
    }

    public double getNumber(String key) {
        ToJSON value = getValue(key);
        if (!(value instanceof NumberJSON))
            throw new RuntimeException("The value of the key " + key + " is not a number");
        return ((NumberJSON) value).getValue();
    }

    public boolean getBoolean(String key) {
        ToJSON value = getValue(key);
        if (!(value instanceof BooleanJSON))
            throw new RuntimeException("The value of the key " + key + " is not a boolean");
        return ((BooleanJSON) value).getValue();
    }

    public String getString(String key) {
        ToJSON value = getValue(key);
        if (!(value instanceof StringJSON))
            throw new RuntimeException("The value of the key " + key + " is not a string");
        return ((StringJSON) value).getValue();
    }

    public ObjectJSON getObject(String key) {
        ToJSON value = getValue(key);
        if (!(value instanceof ObjectJSON))
            throw new RuntimeException("The value of the key " + key + " is not an object");
        return (ObjectJSON) value;
    }

    public ArrayJSON getArray(String key) {
        ToJSON value = getValue(key);
        if (!(value instanceof ArrayJSON))
            throw new RuntimeException("The value of the key " + key + " is not an array");
        return (ArrayJSON) value;
    }

    @Override
    public String toJSON() {
        String json = "{";
        ArrayList<String> keys = new ArrayList<>(values.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            json += "\"" + key + "\":";
            ToJSON value = getValue(key);
            if (value==null)
                json+="null";
            else
                json += getValue(key).toJSON();
            if (i < keys.size() - 1)
                json += ",";
        }
        json += "}";
        return json;
    }
}
