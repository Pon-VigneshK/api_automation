package org.op_ra.utils;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for generating test data using the JavaFaker library.
 */
public final class DataUtils {

    // Private constructor to prevent instantiation of the class
    private DataUtils() {
    }

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
        return "https://" + DataUtils.getRandomAlphabets(10) + ".com";
    }

    public static String getCreditCardNumber() {
        return new Faker().finance().creditCard();
    }

    public static String getCurrentdate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
}
