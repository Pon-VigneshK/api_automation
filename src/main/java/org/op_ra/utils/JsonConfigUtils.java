package org.op_ra.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.op_ra.constants.FrameworkConstants; // Assuming FrameworkConstants provides getJsonConfigFilePath

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Utility class for reading and accessing configuration values from a JSON configuration file.
 * This class is designed to load a JSON file (e.g., {@code jsonConfig.json}) which may contain
 * nested configuration structures, unlike simple key-value pairs in .properties files.
 * <p>
 * The JSON configuration is loaded once into a static map for efficient access.
 * </p>
 * Example JSON structure ({@code jsonConfig.json}):
 * <pre>{@code
 * {
 *   "apiSettings": {
 *     "baseUrl": "http://api.example.com",
 *     "timeoutSeconds": 30
 *   },
 *   "featureFlags": {
 *     "newReportingEnabled": true,
 *     "betaFeatureX": false
 *   }
 * }
 * }</pre>
 * Usage:
 * <pre>{@code
 * Map<String, Object> apiSettings = JsonConfigUtils.getConfigMap("apiSettings");
 * String baseUrl = (String) apiSettings.get("baseUrl");
 *
 * Boolean newReportingFlag = JsonConfigUtils.getNestedValue("featureFlags", "newReportingEnabled", Boolean.class);
 * }</pre>
 */
public final class JsonConfigUtils {

    private static Map<String, Object> jsonConfigMap;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private JsonConfigUtils() {
        // Private constructor
    }

    // Static block to load the JSON configuration file at class loading time.
    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File configFile = new File(FrameworkConstants.getJsonConfigFilePath());
            if (configFile.exists()) {
                jsonConfigMap = objectMapper.readValue(configFile, Map.class);
            } else {
                System.err.println("JsonConfigUtils: JSON config file not found at: " + FrameworkConstants.getJsonConfigFilePath());
                // Initialize with an empty map or throw an exception if the config is critical
                jsonConfigMap = new java.util.HashMap<>();
                // throw new RuntimeException("JSON config file not found: " + FrameworkConstants.getJsonConfigFilePath());
            }
        } catch (IOException e) {
            System.err.println("JsonConfigUtils: Error loading JSON config file: " + FrameworkConstants.getJsonConfigFilePath() + " - " + e.getMessage());
            // Initialize with an empty map or throw
            jsonConfigMap = new java.util.HashMap<>();
            // throw new RuntimeException("Error loading JSON config file: " + FrameworkConstants.getJsonConfigFilePath(), e);
        }
    }

    /**
     * Retrieves the entire configuration map loaded from the JSON file.
     *
     * @return A {@code Map<String, Object>} representing the full JSON configuration.
     *         Returns an empty map if the configuration could not be loaded.
     */
    public static Map<String, Object> getFullConfig() {
        return jsonConfigMap != null ? new java.util.HashMap<>(jsonConfigMap) : new java.util.HashMap<>();
    }

    /**
     * Retrieves a specific top-level configuration section (which itself is a map) from the JSON configuration.
     *
     * @param key The top-level key for the configuration section to retrieve.
     * @return A {@code Map<String, Object>} representing the requested configuration section.
     *         Returns null if the key is not found or the value is not a map.
     *         Returns an empty map if the main config wasn'''t loaded.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getConfigMap(String key) {
        if (jsonConfigMap == null) {
            System.err.println("JsonConfigUtils: Main JSON config map is not loaded.");
            return new java.util.HashMap<>();
        }
        Object value = jsonConfigMap.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        } else {
            // Log warning or error if key not found or not a map
            // System.err.println("JsonConfigUtils: Key '" + key + "' not found or is not a map in JSON config.");
            return null;
        }
    }

    /**
     * Retrieves a value from the JSON configuration using a top-level key.
     *
     * @param key The top-level key whose value is to be retrieved.
     * @return The value associated with the key, or {@code null} if the key is not found.
     *         Returns null if the main config wasn'''t loaded.
     */
    public static Object getValue(String key) {
        if (jsonConfigMap == null) {
             System.err.println("JsonConfigUtils: Main JSON config map is not loaded.");
            return null;
        }
        return jsonConfigMap.get(key);
    }

    /**
     * Retrieves a nested value from the JSON configuration.
     *
     * @param <T> The expected type of the value.
     * @param topLevelKey The key for the top-level JSON object.
     * @param nestedKey   The key within the nested JSON object.
     * @param valueType   The class of the expected type (e.g., String.class, Integer.class, Boolean.class).
     * @return The nested value cast to the specified type, or {@code null} if not found or type mismatch.
     */
    public static <T> T getNestedValue(String topLevelKey, String nestedKey, Class<T> valueType) {
        Map<String, Object> topLevelMap = getConfigMap(topLevelKey);
        if (topLevelMap != null) {
            Object value = topLevelMap.get(nestedKey);
            if (valueType.isInstance(value)) {
                return valueType.cast(value);
            } else if (value != null) {
                // Attempt conversion for common types if direct cast fails (e.g. Integer from Long if JSON parsed as Long)
                if (valueType == Integer.class && value instanceof Number) {
                    return valueType.cast(((Number) value).intValue());
                }
                if (valueType == Long.class && value instanceof Number) {
                    return valueType.cast(((Number) value).longValue());
                }
                if (valueType == Double.class && value instanceof Number) {
                    return valueType.cast(((Number) value).doubleValue());
                }
                 System.err.println("JsonConfigUtils: Type mismatch for nested key '" + nestedKey + "' in '" + topLevelKey + "'. Expected " + valueType.getName() + ", got " + value.getClass().getName());
            }
        }
        return null;
    }
}
