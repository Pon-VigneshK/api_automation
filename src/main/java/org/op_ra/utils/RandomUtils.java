package org.op_ra.utils;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating random strings.
 */
public final class RandomUtils {

    /**
     * Generates a random first name using the JavaFaker library.
     *
     * @return A random first name.
     */
    public static String getFirstName() {
        return new Faker().name().firstName();
    }

    public static String fullName() {
        return new Faker().name().fullName();
    }

    public static String expiryDate() {
        return new Faker().business().creditCardExpiry();
    }

    public static String getLastName() {
        return new Faker().name().lastName();
    }

    /**
     * Generates a random string of digits with the specified count using the JavaFaker library.
     *
     * @param count The number of digits in the generated string.
     * @return A random string of digits.
     */
    public static String getRandomNumber(int count) {
        return new Faker().number().digits(count);
    }

    /**
     * Generates a random string of alphabetic characters with the specified count using
     * the Apache Commons Lang library.
     *
     * @param count The number of alphabetic characters in the generated string.
     * @return A random string of alphabetic characters.
     */
    public static String getRandomAlphabets(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    /**
     * Generates a random website name in the format "https://{random alphabets}.com".
     *
     * @return A random website name.
     */
    public static String getRandomWebsiteName() {
        return "https://" + getRandomAlphabets(10) + ".com";
    }

    public static String getCreditCardNumber() {
        return new Faker().finance().creditCard();
    }

    public static String getCurrentdate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

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

    /**
     * Generates a random string of the specified length using uppercase alphabets,
     * numeric characters, and special characters.
     *
     * @param length The length of the generated string.
     * @return A random alphanumeric string with special characters.
     */
    public static String generateRandomAlphaNumericSpecialString(int length) {
        String characterSet = ",.";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characterSet.charAt(random.nextInt(characterSet.length())));
        }
        return sb.toString();
    }
}
