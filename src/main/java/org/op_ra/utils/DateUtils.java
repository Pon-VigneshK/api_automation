package org.op_ra.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for date and time related operations.
 * Provides methods to get the current date and time in specific formats.
 */
public final class DateUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DateUtils() {
        // Private constructor
    }

    /**
     * Gets the current date and time formatted as "yyyy-MM-dd HH:mm:ss".
     *
     * @return A string representing the current date and time.
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * Gets the current date formatted as "yyyy-MM-dd".
     *
     * @return A string representing the current date.
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * Gets the current date and time formatted according to the specified pattern.
     *
     * @param formatPattern The pattern to format the date and time (e.g., "dd/MM/yyyy HH:mm:ss a").
     *                      See {@link java.text.SimpleDateFormat} for pattern syntax.
     * @return A string representing the current date and time in the given format.
     *         Returns null if the formatPattern is null or invalid (though SimpleDateFormat might throw an exception).
     */
    public static String getCurrentDateTime(String formatPattern) {
        if (formatPattern == null) {
            return null; // Or throw IllegalArgumentException
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
            return sdf.format(new Date());
        } catch (IllegalArgumentException e) {
            // Log error or re-throw
            System.err.println("Invalid date format pattern: " + formatPattern + " - " + e.getMessage());
            return null; // Or throw
        }
    }
}
