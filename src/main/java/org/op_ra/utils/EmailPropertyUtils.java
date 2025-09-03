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

public class EmailPropertyUtils {
    private final static Map<String, String> CONFIG_MAP = new HashMap<>();
    private static final Properties properties = new Properties();
    //    static String configFile = System.getProperty("service.name");
//    static String configFile = "fhir";

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
        // Private constructor to prevent instantiation of the class.
    }

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
