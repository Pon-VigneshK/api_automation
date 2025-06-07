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