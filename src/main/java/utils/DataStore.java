package utils;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private static DataStore instance;
    private final Map<String, String> dataMap;

    private DataStore() {
        dataMap = new HashMap<>();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public Map<String, String> getData() {
        return new HashMap<>(dataMap); // Devuelve una copia para evitar modificaciones externas
    }

    public void clearData() {
        dataMap.clear();
    }

    /**
     * Devuelve el valor asociado a una clave.
     * Si la clave no existe, lanza una excepción.
     * @param key La clave del elemento.
     * @return El valor asociado a la clave.
     * @throws IllegalArgumentException si la clave no existe.
     */
    public String getValue(String key) {
        String normalizedKey = normalizeKey(key);
        System.out.println("Claves actuales en DataStore: " + dataMap.keySet());
        if (!dataMap.containsKey(normalizedKey)) {
            throw new IllegalArgumentException("Clave no encontrada en DataStore: " + key);
        }
        return dataMap.get(normalizedKey);
    }

    /**
     * Añade o actualiza un elemento en DataStore.
     * Si la clave ya existe, actualiza su valor; si no, añade un nuevo par clave-valor.
     * @param key La clave del elemento.
     * @param value El valor del elemento.
     */
    public void setElement(String key, String value) {
        String normalizedKey = normalizeKey(key);
        dataMap.put(normalizedKey, value);
        System.out.println("Clave añadida o actualizada: " + normalizedKey + ", Valor: " + value);
    }

    /**
     * Elimina un elemento por su clave.
     * @param key La clave del elemento a eliminar.
     */
    public void removeElementByKey(String key) {
        String normalizedKey = normalizeKey(key);
        if (dataMap.remove(normalizedKey) != null) {
            System.out.println("Clave eliminada: " + normalizedKey);
        } else {
            System.out.println("Clave no encontrada para eliminar: " + normalizedKey);
        }
    }

    /**
     * Normaliza una clave eliminando espacios en blanco y convirtiéndola a minúsculas.
     * @param key La clave a normalizar.
     * @return La clave normalizada.
     */
    private String normalizeKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("La clave no puede ser nula.");
        }
        return key.trim().toLowerCase();
    }
}
