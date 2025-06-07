package org.op_ra.utils;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.exceptions.PropertyFileUsageException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static org.op_ra.constants.FrameworkConstants.getConfigFilePath;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;

public class PropertyUtils {

    /**
     * Receives the {@link ConfigProperties}, converts it to lowercase, and returns the corresponding value
     * for the key from the HashMap.
     *
     * @param key Key to be fetched from the property file
     * @return Corresponding value for the requested key if found, else {@link PropertyFileUsageException}
     */
    private final static Map<String, String> CONFIG_MAP = new HashMap<>();
    private static final Properties properties = new Properties();
//    static String configFile = System.getProperty("service.name");
    static String configFile = "open";

    static {
        try (FileInputStream file = new FileInputStream(getConfigFilePath(configFile))) {

            log(INFO, "Currently using the " + getConfigFilePath(configFile) + " properties file.");
            properties.load(file);
            for (String key : properties.stringPropertyNames()) {
                CONFIG_MAP.put(String.valueOf(key), String.valueOf(properties.getProperty(key)).trim()); // Remove the trailing and leading spaces
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private PropertyUtils() {
        // Private constructor to prevent instantiation of the class.
    }

    public static String getValue(ConfigProperties key) {
        try {
            if (Objects.isNull(key) || Objects.isNull(CONFIG_MAP.get(key.name().toLowerCase()))) {
            }
        } catch (Exception e) {
            throw new PropertyFileUsageException("Property name " + key + " is not found. Please check the property file");
        }
        return CONFIG_MAP.get(key.name().toLowerCase());

    }
}