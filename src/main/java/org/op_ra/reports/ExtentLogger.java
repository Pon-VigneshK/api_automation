package org.op_ra.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.List;

/**
 * {@code ExtentLogger} is a utility class that provides methods for logging messages to the
 * Extent Report. This class is designed to be used statically and helps in creating a
 * detailed and readable test report.
 *
 * <p>This class uses {@link ExtentManager} to get the current {@link ExtentTest} instance
 * and log messages to it. It provides methods for logging at different levels (pass, fail,
 * skip, info, etc.) and also for logging structured data like tables, JSON, and XML.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To log a message, call the static methods of this class.</p>
 *
 * <pre>{@code
 * ExtentLogger.pass("This step passed successfully.");
 * ExtentLogger.fail("This step failed.");
 * ExtentLogger.logJson(response.asString());
 * }</pre>
 *
 * @see ExtentManager
 * @version 1.0
 * @since 2023-10-27
 */
public class ExtentLogger {

    private ExtentLogger() {
    }

    /**
     * Logs an informational message to the Extent Report with a teal color.
     *
     * @param message The message to log.
     */
    public static void info(String message) {
        ExtentManager.getExtentTest().info(MarkupHelper.createLabel(message, ExtentColor.TEAL));
    }

    /**
     * Logs a pass message to the Extent Report with a green color.
     *
     * @param message The message to log.
     */
    public static void pass(String message) {
        ExtentManager.getExtentTest().pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
    }

    /**
     * Logs a fail message to the Extent Report with a red color.
     *
     * @param message The message to log.
     */
    public static void fail(String message) {
        ExtentManager.getExtentTest().fail(MarkupHelper.createLabel(message, ExtentColor.RED));
    }

    /**
     * Logs a warning message to the Extent Report with a yellow color.
     *
     * @param message The message to log.
     */
    public static void warn(String message) {
        ExtentManager.getExtentTest().warning(MarkupHelper.createLabel(message, ExtentColor.YELLOW));
    }

    /**
     * Logs an informational message to the Extent Report with a lime color.
     *
     * @param message The message to log.
     */
    public static void log(String message) {
        ExtentManager.getExtentTest().info(MarkupHelper.createLabel(message, ExtentColor.LIME));
    }

    /**
     * Logs a skipped message to the Extent Report with a pink color.
     *
     * @param message The message to log.
     */
    public static void skip(String message) {
        ExtentManager.getExtentTest().skip(MarkupHelper.createLabel(message, ExtentColor.PINK));
    }

    /**
     * Logs a table with the given data to the Extent Report.
     *
     * @param message A 2D array representing the table data.
     */
    public static void logInfoTable(String[][] message) {
        ExtentManager.getExtentTest().info(MarkupHelper.createTable(message));
    }

    /**
     * Logs a stack trace in a formatted way to the Extent Report.
     *
     * @param message The stack trace information as a string.
     */
    public static void logStackTraceInfoInExtentReport(String message) {
        String formattedText = "<pre>" + message.replace(",", "<br>") + "</pre>";
        ExtentManager.getExtentTest().fail(formattedText);
    }

    /**
     * Logs the headers of a REST Assured response to the Extent Report in a table format.
     *
     * @param response The REST Assured response object.
     */
    public static void logHeaders(Response response) {
        List<Header> headersList = response.getHeaders().asList();

        if (headersList == null || headersList.isEmpty()) {
            ExtentManager.getExtentTest().info("No headers found in the response.");
            return;
        }

        StringBuilder tableBuilder = new StringBuilder();

        tableBuilder.append("<table style='border-collapse: collapse; width: 100%; font-family: Arial, sans-serif;'>")
                .append("<thead>")
                .append("<tr style='background-color: #f2f2f2;'>")
                .append("<th style='border: 1px solid #ddd; padding: 8px; color: black;'>Header Name</th>")
                .append("<th style='border: 1px solid #ddd; padding: 8px; color: black;'>Header Value</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        for (Header header : headersList) {
            tableBuilder.append("<tr>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px; color: black;'>")
                    .append(header.getName())
                    .append("</td>")
                    .append("<td style='border: 1px solid #ddd; padding: 8px; color: black;'>")
                    .append(header.getValue())
                    .append("</td>")
                    .append("</tr>");
        }

        tableBuilder.append("</tbody></table>");

        ExtentManager.getExtentTest().info("Response Headers are:");
        ExtentManager.getExtentTest().info(tableBuilder.toString());
    }

    /**
     * Logs the response headers to the Extent Report.
     *
     * @param response The REST Assured response object.
     */
    public static void logResponseToReport(Response response) {
        log("Response Headers are :");
        logHeaders(response);
    }

    /**
     * Logs the response body in a pretty JSON format to the Extent Report.
     *
     * @param response The REST Assured response object.
     */
    public static void logPrettyJsonResponseToReport(Response response) {
        log("Response body is : ");
        logJson(response.asPrettyString());
    }

    /**
     * Logs a JSON string as a code block in the Extent Report.
     *
     * @param json The JSON string to log.
     */
    public static void logJson(String json) {
        ExtentManager.getExtentTest().info(MarkupHelper.createCodeBlock(json, CodeLanguage.JSON));
    }

    /**
     * Logs an XML string as a code block in the Extent Report.
     *
     * @param response The XML string to log.
     */
    public static void logXml(String response) {
        ExtentManager.getExtentTest().info(MarkupHelper.createCodeBlock(response, CodeLanguage.XML));
    }

    /**
     * Logs the entire response (status, headers, and body) in a pretty XML format to the Extent Report.
     *
     * @param response The REST Assured response object.
     */
    public static void logPrettyXmlResponseToReport(Response response) {
        log("Response status is " + response.getStatusCode());
        log("Response Headers are :");
        logHeaders(response);
        log("Response body is : ");
        logXml(response.asPrettyString());
    }
}
