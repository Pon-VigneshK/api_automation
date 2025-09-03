package org.op_ra.utils;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * {@code RandomUtils} is a final class that provides utility methods for generating random data.
 * This class uses the JavaFaker and Apache Commons Lang libraries to generate various types
 * of random data, such as names, numbers, and strings.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to generate random data for tests.</p>
 *
 * <pre>{@code
 * // Generating a random first name
 * String firstName = RandomUtils.getFirstName();
 *
 * // Generating a random number
 * String randomNumber = RandomUtils.getRandomNumber(5);
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public final class RandomUtils {

    private static final SecureRandom random = new SecureRandom();
    private static final String NUMERIC_STRING = "0123456789";
    private static final String ALPHABET_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private RandomUtils() {
    }

    /**
     * Generates a random first name using the JavaFaker library.
     *
     * @return A random first name.
     */
    public static String getFirstName() {
        return new Faker().name().firstName();
    }

    /**
     * Generates a random full name using the JavaFaker library.
     *
     * @return A random full name.
     */
    public static String fullName() {
        return new Faker().name().fullName();
    }

    /**
     * Generates a random credit card expiry date using the JavaFaker library.
     *
     * @return A random credit card expiry date.
     */
    public static String expiryDate() {
        return new Faker().business().creditCardExpiry();
    }

    /**
     * Generates a random last name using the JavaFaker library.
     *
     * @return A random last name.
     */
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

    /**
     * Generates a random credit card number using the JavaFaker library.
     *
     * @return A random credit card number.
     */
    public static String getCreditCardNumber() {
        return new Faker().finance().creditCard();
    }

    /**
     * Returns the current date as a string in "yyyy-MM-dd" format.
     *
     * @return The current date as a string.
     */
    public static String getCurrentdate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
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
            sb.append(ALPHABET_STRING.charAt(random.nextInt(ALPHABET_STRING.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random string of the specified length using a limited set of special characters.
     *
     * @param length The length of the generated string.
     * @return A random string with special characters.
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
