package org.op_ra.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * {@code DateUtils} is a final class that provides utility methods for working with dates.
 * This class includes methods for converting and formatting dates.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to perform various date operations.</p>
 *
 * <pre>{@code
 * // Converting a date string to a different format
 * String formattedDate = DateUtils.convertDate("10/27/2023 10:00", "M/dd/yyyy HH:mm");
 *
 * // Adjusting a date by a certain number of days
 * String adjustedDate = DateUtils.adjustDate("2023-10-27", "5");
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public final class DateUtils {

    /**
     * Converts a date string from "M/dd/yyyy HH:mm" format to "yyyy-MM-dd" format.
     *
     * @param inputDate The date string to convert.
     * @return The formatted date string.
     */
    public static String convertDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("M/dd/yyyy HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = null;
        try {
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return formattedDate;
    }

    /**
     * Converts a date string from a given format to "yyyy-MM-dd" format.
     *
     * @param inputDate The date string to convert.
     * @param format The format of the input date string.
     * @return The formatted date string.
     */
    public static String convertDate(String inputDate, String format) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(format);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = null;
        try {
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return formattedDate;
    }

    /**
     * Formats a date string from "yyyy-MM-dd HH:mm:ss" to "yyyy-MM-dd'T'HH:mm:ss".
     *
     * @param input The date string to format.
     * @return The formatted date string.
     */
    public static String formatDate(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTime.format(outputFormatter);

    }

    /**
     * Adjusts a date string by a given number of days.
     *
     * @param dateStr The date string to adjust, in "yyyy-MM-dd" format.
     * @param daysStr The number of days to add, as a string.
     * @return The adjusted date string.
     */
    public static String adjustDate(String dateStr, String daysStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        int days = Integer.parseInt(daysStr.trim());
        LocalDate adjustedDate = date.plusDays(days);
        return adjustedDate.format(formatter);
    }
}
