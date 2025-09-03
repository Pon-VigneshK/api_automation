package org.op_ra.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.List;

/**
 * The ExtentLogger class helps achieve a test report for the current test.
 *
 * @see ExtentManager
 */

public class ExtentLogger {

    private ExtentLogger() {
        // Private constructor to prevent instantiation
    }

    public static void info(String message) {
        ExtentManager.getExtentTest().info(MarkupHelper.createLabel(message, ExtentColor.TEAL));
    }

    /**
     * Log a pass message to the Extent Report.
     *
     * @param message Message to log.
     */
    public static void pass(String message) {
        ExtentManager.getExtentTest().pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
    }

    /**
     * Log a fail message to the Extent Report.
     *
     * @param message Message to log.
     */
    public static void fail(String message) {
        ExtentManager.getExtentTest().fail(MarkupHelper.createLabel(message, ExtentColor.RED));
    }

    /**
     * Log a warning message to the Extent Report.
     *
     * @param message Message to log.
     */
    public static void warn(String message) {
        ExtentManager.getExtentTest().warning(MarkupHelper.createLabel(message, ExtentColor.YELLOW));
    }

    /**
     * Log an informational message to the Extent Report.
     *
     * @param message Message to log.
     */
    public static void log(String message) {
        ExtentManager.getExtentTest().info(MarkupHelper.createLabel(message, ExtentColor.LIME));
    }

    /**
     * Log a skipped message to the Extent Report.
     *
     * @param message Message to log.
     */
    public static void skip(String message) {
        ExtentManager.getExtentTest().skip(MarkupHelper.createLabel(message, ExtentColor.PINK));
    }

    // Private method for logging with specified color
    private static void logWithColor(String message, ExtentColor color) {
        ExtentTest extentTest = ExtentManager.getExtentTest();
        if (extentTest != null) {
            extentTest.info(MarkupHelper.createLabel(message, color));
        } else {
            // Log to console or handle appropriately if extentTest is null
            System.out.println("ExtentTest is null. Unable to log message: " + message);
        }
    }

    /**
     * Log a table with information to the Extent Report.
     *
     * @param message 2D array representing the table data.
     */
    public static void logInfoTable(String[][] message) {
        ExtentManager.getExtentTest().info(MarkupHelper.createTable(message));
    }

    /**
     * Log a stack trace information in Extent Report.
     *
     * @param message Stack trace information.
     */
    public static void logStackTraceInfoInExtentReport(String message) {
        String formattedText = "<pre>" + message.replace(",", "<br>") + "</pre>";
        ExtentManager.getExtentTest().fail(formattedText);
    }


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
                .append("<th style='border: 1px solid #ddd; padding: 8px; color: black;'>Header Value</th>") // ✅ THIS LINE MATTERS!
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
     * Log the response information to the Extent Report.
     *
     * @param response Response object to log.
     */
    public static void logResponseToReport(Response response) {
        log("Response Headers are :");
        logHeaders(response);
    }

    /**
     * Log the pretty JSON response information to the Extent Report.
     *
     * @param response Response object to log.
     */
    public static void logPrettyJsonResponseToReport(Response response) {
        log("Response body is : ");
        logJson(response.asPrettyString());
    }

    /**
     * Log JSON content to the Extent Report.
     *
     * @param json JSON content to log.
     */
    public static void logJson(String json) {
        ExtentManager.getExtentTest().info(MarkupHelper.createCodeBlock(json, CodeLanguage.JSON));
    }

    /**
     * Log XML content to the Extent Report.
     *
     * @param response XML content to log.
     */
    public static void logXml(String response) {
        ExtentManager.getExtentTest().info(MarkupHelper.createCodeBlock(response, CodeLanguage.XML));
    }

    /**
     * Log the pretty XML response information to the Extent Report.
     *
     * @param response Response object to log.
     */
    public static void logPrettyXmlResponseToReport(Response response) {
        log("Response status is " + response.getStatusCode());
        log("Response Headers are :");
        logHeaders(response);
        log("Response body is : ");
        logXml(response.asPrettyString());
    }
}
