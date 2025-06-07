package org.op_ra.utils;

// Assuming Faker might be used here, or other data generation libraries.
// import com.github.javafaker.Faker;

/**
 * Utility class for generating or manipulating common test data types.
 * This class can provide methods for creating random strings, numbers,
 * or more complex data structures needed for tests, potentially complementing
 * {@link RandomUtils} or {@link JsonUtils}.
 * <p>
 * <b>Note:</b> The current content of this file is a placeholder.
 * It should be populated with actual data utility methods relevant to the framework.
 * </p>
 * Example uses:
 * <ul>
 *     <li>Generating data in specific formats (e.g., UUIDs, formatted dates beyond DateUtils).</li>
 *     <li>Creating collections of test data objects.</li>
 *     <li>Transforming data from one format to another.</li>
 * </ul>
 */
public final class DataUtils {

    // private static final Faker faker = new Faker(); // If using Faker

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DataUtils() {
        // Private constructor
    }

    /**
     * Example: Generates a random alphanumeric string of a specified length.
     *
     * @param length The desired length of the string.
     * @return A random alphanumeric string.
     * @deprecated This is an example method. Implement with actual random string generation logic.
     */
    @Deprecated
    public static String generateRandomAlphanumericString(int length) {
        if (length <= 0) {
            return "";
        }
        // Replace with actual implementation, e.g., using Apache Commons Lang RandomStringUtils
        // return RandomStringUtils.randomAlphanumeric(length);
        System.out.println("DataUtils.generateRandomAlphanumericString is a placeholder. Length: " + length);
        // Simple placeholder implementation:
        StringBuilder sb = new StringBuilder(length);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Example: Generates a random number within a given range as a string.
     *
     * @param min Minimum value (inclusive).
     * @param max Maximum value (inclusive).
     * @return A string representation of a random number in the range.
     * @deprecated This is an example method. Consider using {@link RandomUtils#getRandomNumber(int, int)}
     *             if integer output is acceptable, or refine this for specific string formatted numbers.
     */
    @Deprecated
    public static String generateRandomNumericString(int min, int max) {
        // return String.valueOf(faker.number().numberBetween(min, max));
        System.out.println("DataUtils.generateRandomNumericString is a placeholder. Min: " + min + ", Max: " + max);
        java.util.Random random = new java.util.Random();
        return String.valueOf(random.nextInt((max - min) + 1) + min);
    }

    // Add other relevant data utility methods here.
    // For example, methods to:
    // - Get current timestamp in a specific format (if not covered by DateUtils)
    // - Generate unique IDs (UUIDs)
    // - Manipulate collections or arrays of data for testing.
}
