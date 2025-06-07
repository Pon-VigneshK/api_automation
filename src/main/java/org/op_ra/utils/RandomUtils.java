package org.op_ra.utils;

import com.github.javafaker.Faker;

/**
 * Utility class for generating random test data using the JavaFaker library.
 * Provides methods to get various types of fake data such as names, numbers, and more.
 * This helps in creating dynamic and realistic test data.
 */
public final class RandomUtils {

    // Static instance of Faker to be reused.
    private static final Faker faker = new Faker();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RandomUtils() {
        // Private constructor
    }

    /**
     * Generates a random integer within a specified range (inclusive).
     *
     * @param min The minimum value (inclusive).
     * @param max The maximum value (inclusive).
     * @return A random integer between min and max.
     */
    public static int getRandomNumber(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    /**
     * Generates a random first name.
     *
     * @return A string representing a random first name.
     */
    public static String getFirstName() {
        return faker.name().firstName();
    }

    /**
     * Generates a random last name.
     *
     * @return A string representing a random last name.
     */
    public static String getLastName() {
        return faker.name().lastName();
    }

    /**
     * Generates a random full name.
     *
     * @return A string representing a random full name.
     */
    public static String getFullName() {
        return faker.name().fullName();
    }

    /**
     * Generates a random email address.
     *
     * @return A string representing a random email address.
     */
    public static String getEmailAddress() {
        return faker.internet().emailAddress();
    }

    /**
     * Generates a random street address.
     *
     * @return A string representing a random street address.
     */
    public static String getStreetAddress() {
        return faker.address().streetAddress();
    }

    /**
     * Generates a random city name.
     *
     * @return A string representing a random city name.
     */
    public static String getCityName() {
        return faker.address().cityName();
    }

    /**
     * Generates a random country name.
     *
     * @return A string representing a random country name.
     */
    public static String getCountryName() {
        return faker.address().country();
    }

    /**
     * Generates a random job title.
     *
     * @return A string representing a random job title.
     */
    public static String getJobTitle() {
        return faker.job().title();
    }

    // Add more methods as needed for other types of fake data, e.g.:
    // faker.phoneNumber().cellPhone();
    // faker.lorem().sentence();
    // faker.date().birthday();
}
