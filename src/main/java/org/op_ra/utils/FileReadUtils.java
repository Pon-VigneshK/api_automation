package org.op_ra.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class for reading content from files.
 */
public final class FileReadUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FileReadUtils() {
        // Private constructor
    }

    /**
     * Reads the entire content of a text file into a single string.
     * Each line from the file will be appended followed by a newline character.
     *
     * @param filePath The absolute or relative path to the text file.
     * @return A string containing the content of the file.
     * @throws IOException If an error occurs during file reading (e.g., file not found, permission issues).
     *                     The caller should handle this exception.
     */
    public static String readFileToString(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append(System.lineSeparator());
            }
        }
        // IOException from BufferedReader or FileReader constructor will propagate
        return contentBuilder.toString();
    }

    // Add more methods if needed, e.g., reading a file into a list of strings,
    // reading properties files (though PropertyUtils handles .properties specifically).
}
