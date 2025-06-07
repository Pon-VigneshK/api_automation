package org.op_ra.utils;

import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.ConfigProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Utility class for reading and managing configuration properties from .properties files.
 * It loads properties from a file specified by {@link FrameworkConstants#getConfigFilePath()}
 * and provides a type-safe way to access them using the {@link ConfigProperties} enum.
 * <p>
 * Properties are loaded once and cached in a static map for efficient access.
 * If the properties file cannot be loaded, a {@link RuntimeException} is thrown.
 * </p>
 */
public final class PropertyUtils {

    private static final Properties property = new Properties();
    private static final Map<String, String> CONFIGMAP = new HashMap<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PropertyUtils() {
        // Private constructor
    }

    // Static block to load properties from the file at class loading time.
    static {
        try (FileInputStream file = new FileInputStream(FrameworkConstants.getConfigFilePath())) {
            property.load(file);
            // Populate the CONFIGMAP from the loaded properties
            for (Map.Entry<Object, Object> entry : property.entrySet()) {
                CONFIGMAP.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()).trim()); // Trim whitespace
            }
        } catch (FileNotFoundException e) {
            // It'''s generally better to throw a more specific custom exception or log an error
            // and then throw RuntimeException if the application cannot proceed without these properties.
            System.err.println("ERROR: Properties file not found at: " + FrameworkConstants.getConfigFilePath());
            // Depending on the application'''s needs, you might exit or throw a critical error.
            throw new RuntimeException("Properties file not found: " + FrameworkConstants.getConfigFilePath(), e);
        } catch (IOException e) {
            System.err.println("ERROR: IOException while loading properties file: " + FrameworkConstants.getConfigFilePath());
            throw new RuntimeException("IOException while loading properties file: " + FrameworkConstants.getConfigFilePath(), e);
        }
    }

    /**
     * Retrieves the value of a configuration property specified by a {@link ConfigProperties} key.
     * <p>
     * This method looks up the property value from the cached configuration map.
     * If the key is not found in the map or if the corresponding enum key is null,
     * it throws a {@link RuntimeException} indicating that the property is not specified.
     * </p>
     *
     * @param key The {@link ConfigProperties} enum representing the desired property key.
     *            The actual lookup is done using {@code key.name().toLowerCase()}.
     * @return The trimmed string value of the property.
     * @throws RuntimeException if the property key is null, not found in the configuration file,
     *                          or if its value is null (empty values might be acceptable depending on use case).
     */
    public static String getValue(ConfigProperties key) {
        if (Objects.isNull(key) || Objects.isNull(CONFIGMAP.get(key.name().toLowerCase()))) {
            // Log or throw a more specific exception, e.g., ConfigurationNotFoundException
            throw new RuntimeException("Property name - " + (key != null ? key.name().toLowerCase() : "null_key") + " - not found. Please check config.properties");
        }
        return CONFIGMAP.get(key.name().toLowerCase());
    }

    // It might be useful to have a method that returns a value with a default if not found,
    // or a method to check if a key exists, depending on how strictly configurations are managed.
    // Example:
    // public static String getValueOrDefault(ConfigProperties key, String defaultValue) {
    //     return CONFIGMAP.getOrDefault(key.name().toLowerCase(), defaultValue);
    // }
}
