package org.op_ra.utils;

import org.op_ra.enums.EmailProperties;
import org.op_ra.exceptions.PropertyFileUsageException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static org.op_ra.constants.FrameworkConstants.getMailConfig;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;

/**
 * {@code EmailPropertyUtils} is a utility class for reading email configuration properties from a file.
 * This class loads the properties from the email configuration file and provides a method to retrieve
 * the value of a specific property.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To get a value from the email properties file, use the {@code getValue} method with an
 * {@link EmailProperties} enum key.</p>
 *
 * <pre>{@code
 * String fromEmail = EmailPropertyUtils.getValue(EmailProperties.FROMEMAIL);
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class EmailPropertyUtils {
    private final static Map<String, String> CONFIG_MAP = new HashMap<>();
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream file = new FileInputStream(getMailConfig())) {

            log(INFO, "Currently using the " + getMailConfig() + " properties file.");
            properties.load(file);
            for (String key : properties.stringPropertyNames()) {
                CONFIG_MAP.put(String.valueOf(key), String.valueOf(properties.getProperty(key)).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private EmailPropertyUtils() {
    }

    /**
     * Returns the value of the specified email property.
     *
     * @param key The key of the property to retrieve, as an {@link EmailProperties} enum.
     * @return The value of the property.
     * @throws PropertyFileUsageException if the key is not found in the properties file.
     */
    public static String getValue(EmailProperties key) {
        try {
            if (Objects.isNull(key) || Objects.isNull(CONFIG_MAP.get(key.name().toLowerCase()))) {
            }
        } catch (Exception e) {
            throw new PropertyFileUsageException("Property name " + key + " is not found. Please check the property file");
        }
        return CONFIG_MAP.get(key.name().toLowerCase());
    }
}
