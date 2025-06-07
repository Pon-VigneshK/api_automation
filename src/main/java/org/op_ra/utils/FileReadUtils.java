package org.op_ra.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileReadUtils {
    private FileReadUtils() {
    }

    public static String getPayLoadFileLocationAsString(String location) {
        try {
            return new String(Files.readAllBytes(Paths.get(location)));
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return null;
        }
    }
}
