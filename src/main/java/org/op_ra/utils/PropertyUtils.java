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

/**
 * {@code PropertyUtils} is a utility class for reading configuration properties from a file.
 * This class loads the properties from the config file and provides a method to retrieve
 * the value of a specific property.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To get a value from the properties file, use the {@code getValue} method with a
 * {@link ConfigProperties} enum key.</p>
 *
 * <pre>{@code
 * String baseUrl = PropertyUtils.getValue(ConfigProperties.BASEURL);
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class PropertyUtils {

    private final static Map<String, String> CONFIG_MAP = new HashMap<>();
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream file = new FileInputStream(getConfigFilePath())) {

            log(INFO, "Currently using the " + getConfigFilePath() + " properties file.");
            properties.load(file);
            for (String key : properties.stringPropertyNames()) {
                CONFIG_MAP.put(String.valueOf(key), String.valueOf(properties.getProperty(key)).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private PropertyUtils() {
    }

    /**
     * Returns the value of the specified property from the configuration file.
     *
     * @param key The key of the property to retrieve, as a {@link ConfigProperties} enum.
     * @return The value of the property.
     * @throws PropertyFileUsageException if the key is not found in the properties file.
     */
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