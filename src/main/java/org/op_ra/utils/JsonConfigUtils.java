package org.op_ra.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.DataBaseProperties;
import org.op_ra.exceptions.JsonExceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@code JsonConfigUtils} is a utility class for reading database configuration from a JSON file.
 * This class loads the configuration from the `DataBaseConfig.json` file and provides a method
 * to retrieve the value of a specific property.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To get a value from the JSON config file, use the {@code get} method with a
 * {@link DataBaseProperties} enum key.</p>
 *
 * <pre>{@code
 * String hostname = JsonConfigUtils.get(DataBaseProperties.HOSTNAME);
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class JsonConfigUtils {
    private static Map<String, String> CONFIGMAP;

    static {
        try {
            CONFIGMAP = new ObjectMapper().readValue(
                    new File(FrameworkConstants.getDBConfigJSONPath()),
                    new TypeReference<HashMap<String, String>>() {
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonConfigUtils() {
    }

    /**
     * Returns the value of the specified database property from the JSON configuration file.
     *
     * @param key The key of the property to retrieve, as a {@link DataBaseProperties} enum.
     * @return The value of the property.
     * @throws JsonExceptions if the key is null or not found in the JSON file.
     */
    public static String get(DataBaseProperties key) throws JsonExceptions {
        if (Objects.isNull(key)) {
            throw new JsonExceptions("Key is null. Please provide a valid key.");
        }

        String value = CONFIGMAP.get(key.name().toLowerCase());
        if (Objects.isNull(value)) {
            throw new JsonExceptions("Property name " + key + " is not found in the JSON file. Please check the DataBaseConfig.json file.");
        }

        return value;
    }
}