package org.op_ra.utils;

import java.security.SecureRandom;

/**
 * Utility class for generating random strings.
 */
public final class RandomUtils {

    // SecureRandom instance for generating random numbers
    private static final SecureRandom random = new SecureRandom();

    // String containing all numeric characters
    private static final String NUMERIC_STRING = "0123456789";

    // String containing all uppercase alphabet characters
    private static final String ALPHABET_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Private constructor to prevent instantiation
    private RandomUtils() {
    }

    /**
     * Generates a random numeric string of the specified length.
     *
     * @param length The length of the generated numeric string.
     * @return A random numeric string.
     */
    public static String generateRandomNumericString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // Append a random numeric character to the StringBuilder
            sb.append(NUMERIC_STRING.charAt(random.nextInt(NUMERIC_STRING.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random string of the specified length using uppercase alphabet characters.
     *
     * @param length The length of the generated string.
     * @return A random string.
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // Append a random alphabet character to the StringBuilder
            sb.append(ALPHABET_STRING.charAt(random.nextInt(ALPHABET_STRING.length())));
        }
        return sb.toString();
    }
}
