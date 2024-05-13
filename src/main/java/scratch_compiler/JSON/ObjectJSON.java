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
        if (value == this)
            throw new RuntimeException("Cannot add an object to itself");

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

    public void add(ObjectJSON object) {
        ArrayList<String> keys = new ArrayList<>(object.values.keySet());
        for (String key : keys) {
            if (contains(key)) {
                System.out.println(key + " " + object.getValue(key).toJSON());
                System.out.println(getValue(key).toJSON());
                // throw new RuntimeException("The key " + key + " already exists");
                System.out.println("The key " + key + " already exists");
                continue;
            }

            setValue(key, object.getValue(key));
        }
    }

    public ArrayList<String> getKeys() {
        return new ArrayList<>(values.keySet());
    }

    @Override
    public String toJSON() {
        String json = "{";
        ArrayList<String> keys = new ArrayList<>(values.keySet());
        if (keys.size() > 1)
            json += "\n";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (keys.size() > 1)
                json += "\t";
            json += "\"" + key + "\": ";
            ToJSON value = getValue(key);
            if (value == null)
                json += "null";
            else
                json += getValue(key).toJSON().replace("\n", "\n\t");
            if (i < keys.size() - 1)
                json += ",\n";
        }
        if (keys.size() > 1)
            json += "\n";
        json += "}";
        return json;
    }
}
