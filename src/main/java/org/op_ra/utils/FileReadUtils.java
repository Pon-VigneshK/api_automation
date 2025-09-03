package org.op_ra.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * {@code FileReadUtils} is a final class that provides a utility method for reading the
 * content of a file and returning it as a string.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The {@code getPayLoadFileLocationAsString} method can be called to read the content of a file.</p>
 *
 * <pre>{@code
 * String fileContent = FileReadUtils.getPayLoadFileLocationAsString("path/to/file.txt");
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public final class FileReadUtils {
    private FileReadUtils() {
    }

    /**
     * Reads the content of a file at the specified location and returns it as a string.
     *
     * @param location The path to the file to be read.
     * @return The content of the file as a string, or {@code null} if an error occurs.
     */
    public static String getPayLoadFileLocationAsString(String location) {
        try {
            return new String(Files.readAllBytes(Paths.get(location)));
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            return null;
        }
    }
}
