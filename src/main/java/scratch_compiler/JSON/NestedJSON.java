package scratch_compiler.JSON;

import java.util.ArrayList;
import java.util.HashMap;

public class NestedJSON implements ToJSON {
    private ArrayList<String> keys;
    private HashMap<String, Double> doubles;
    private HashMap<String, Integer> integers;
    private HashMap<String, String> strings;
    private HashMap<String, Boolean> booleans;
    private HashMap<String, NestedJSON> nested;
    private HashMap<String, ArrayJSON> arrays;

    public NestedJSON() {
        keys = new ArrayList<String>();
        doubles= new HashMap<String, Double>();
        integers = new HashMap<String, Integer>();
        strings = new HashMap<String, String>();
        booleans = new HashMap<String, Boolean>();
        nested = new HashMap<String, NestedJSON>();
        arrays = new HashMap<String, ArrayJSON>();
    }

    private boolean contains(String key) {
        return keys.contains(key);
    }

    public boolean isDouble(String key) {
        return doubles.containsKey(key);
    }

    public boolean isInteger(String key) {
        return integers.containsKey(key);
    }

    public boolean isString(String key) {
        return strings.containsKey(key);
    }

    public boolean isBoolean(String key) {
        return booleans.containsKey(key);
    }

    public boolean isNested(String key) {
        return nested.containsKey(key);
    }

    public boolean isArray(String key) {
        return arrays.containsKey(key);
    }

    public void remove(String key) {
        if (contains(key)) {
            keys.remove(key);
            if (isDouble(key))
                doubles.remove(key);
            if (isInteger(key))
                integers.remove(key);
            if (isString(key))
                strings.remove(key);
            if (isBoolean(key))
                booleans.remove(key);
            if (isNested(key))
                nested.remove(key);
            if (isArray(key))
                arrays.remove(key);
        }
    }

    public void setDouble(String key, double value) {
        remove(key);
        keys.add(key);
        doubles.put(key, value);
    }

    public double getDouble(String key) {
        if (isDouble(key))
            return doubles.get(key);
        if (isInteger(key))
            return integers.get(key);
        return 0;
    }

    public void setInteger(String key, int value) {
        remove(key);
        keys.add(key);
        integers.put(key, value);
    }

    public int getInteger(String key) {
        if (isInteger(key))
            return integers.get(key);
        return 0;
    }

    public void setString(String key, String value) {
        remove(key);
        keys.add(key);
        strings.put(key, value);
    }

    public String getString(String key) {
        if (isString(key))
            return strings.get(key);
        return "";
    }

    public void setBoolean(String key, boolean value) {
        remove(key);
        keys.add(key);
        booleans.put(key, value);
    }

    public boolean getBoolean(String key) {
        if (isBoolean(key))
            return booleans.get(key);
        return false;
    }

    public void setNested(String key, NestedJSON value) {
        remove(key);
        keys.add(key);
        nested.put(key, value);
    }

    public NestedJSON getNested(String key) {
        if (isNested(key))
            return nested.get(key);
        return new NestedJSON();
    }

    public void setArray(String key, ArrayJSON value) {
        remove(key);
        keys.add(key);
        arrays.put(key, value);
    }

    public ArrayJSON getArray(String key) {
        if (isArray(key))
            return arrays.get(key);
        return new ArrayJSON();
    }



    @Override
    public String toJSON() {
        String json = "{";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            json += "\"" + key + "\": ";
            if (isDouble(key))
                json += doubles.get(key);
            if (isInteger(key))
                json += integers.get(key);
            if (isString(key))
            {
                String value = strings.get(key);
                if (value==null)
                    json += "null";
                else if (value.startsWith("§"))
                    json += value.substring(1);
                else
                    json += "\"" + value + "\"";
            }
            if (isBoolean(key))
                json += booleans.get(key);
            if (isNested(key))
                json += nested.get(key).toJSON();
            if (isArray(key))
                json += arrays.get(key).toJSON();

            if (i != keys.size() - 1)
                json += ", ";
        }
        json += "}";
        return json;
    }
}
