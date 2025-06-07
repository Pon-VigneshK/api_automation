package org.op_ra.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateUtils {
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

    public static String formatDate(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTime.format(outputFormatter);

    }
}
