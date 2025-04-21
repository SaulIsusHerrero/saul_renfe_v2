package utils;

import java.util.HashMap;
import java.util.Map;

public class TemporaryDataStore {
    private static TemporaryDataStore instance;
    private Map<String, Object> data;

    private TemporaryDataStore() {
        data = new HashMap<>();
    }

    public static synchronized TemporaryDataStore getInstance() {
        if (instance == null) {
            instance = new TemporaryDataStore();
        }
        return instance;
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void clear() {
        data.clear();
    }
}
