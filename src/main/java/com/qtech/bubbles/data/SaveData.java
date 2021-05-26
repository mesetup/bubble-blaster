package com.qtech.bubbles.data;

import com.qtech.utilities.python.builtins.ValueError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Quinten Jungblut
 */
public class SaveData {
    public final HashMap<List<String>, Object> saveData = new HashMap<>();

    public String getString(String... path) {
        ArrayList<String> stringArray = new ArrayList<>(Arrays.asList(path));

        if (!saveData.containsKey(Arrays.asList(path))) {
            return null;
        }

        Object rawData = saveData.get(stringArray);
        if (rawData instanceof String) {
            return (String) rawData;
        } else {
            throw new ValueError("Data is not instance of String");
        }
    }

    public Integer getInt(String... path) {
        ArrayList<String> stringArray = new ArrayList<>(Arrays.asList(path));

        if (!saveData.containsKey(Arrays.asList(path))) {
            return null;
        }

        Object rawData = saveData.get(stringArray);
        if (rawData instanceof Integer) {
            return (Integer) rawData;
        } else {
            throw new ValueError("Data is not instance of Integer");
        }
    }

    public Float getFloat(String... path) {
        ArrayList<String> stringArray = new ArrayList<>(Arrays.asList(path));

        if (!saveData.containsKey(Arrays.asList(path))) {
            return null;
        }

        Object rawData = saveData.get(stringArray);
        if (rawData instanceof Float) {
            return (Float) rawData;
        } else {
            throw new ValueError("Data is not instance of Float");
        }
    }

    public Double getDouble(String... path) {
        ArrayList<String> stringArray = new ArrayList<>(Arrays.asList(path));

        if (!saveData.containsKey(Arrays.asList(path))) {
            return null;
        }

        Object rawData = saveData.get(stringArray);
        if (rawData instanceof Double) {
            return (Double) rawData;
        } else {
            throw new ValueError("Data is not instance of Double");
        }
    }

    public Boolean getBoolean(String... path) {
        ArrayList<String> stringArray = new ArrayList<>(Arrays.asList(path));

        if (!saveData.containsKey(Arrays.asList(path))) {
            return null;
        }

        Object rawData = saveData.get(stringArray);
        if (rawData instanceof Boolean) {
            return (Boolean) rawData;
        } else {
            throw new ValueError("Data is not instance of Boolean");
        }
    }

    public Object getObject(String... path) {
        ArrayList<String> stringArray = new ArrayList<>(Arrays.asList(path));

        if (!saveData.containsKey(Arrays.asList(path))) {
            return null;
        }

        Object rawData = saveData.get(stringArray);
        if (rawData != null) {
            return rawData;
        } else {
            throw new ValueError("Data is not instance of Object");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<? extends T> getArrayList(String... path) {
        ArrayList<String> stringArray = new ArrayList<>(Arrays.asList(path));

        if (!saveData.containsKey(Arrays.asList(path))) {
            return null;
        }

        Object rawData = saveData.get(stringArray);
        if (rawData instanceof ArrayList) {
            return (ArrayList<T>) rawData;
        } else {
            throw new ValueError("Data is not instance of ArrayList");
        }
    }
}
