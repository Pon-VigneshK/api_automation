package org.op_ra.reports;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.utils.PropertyUtils; // Assuming PropertyUtils is in utils

import java.util.List;

/**
 * Provides static methods to log messages and details to ExtentReports.
 * This class acts as a wrapper around {@link ExtentManager} to simplify logging
 * common types of information such as pass/fail status, informational messages,
 * screenshots, and API request/response details.
 * <p>
 * It ensures that logs are associated with the correct test in the report by using
 * the thread-safe {@link ExtentManager#getExtentTest()}.
 * </p>
 */
public final class ExtentLogger {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ExtentLogger() {
        // Private constructor
    }

    /**
     * Logs a message with PASS status in the Extent report.
     *
     * @param message The message to log.
     */
    public static void pass(String message) {
        if (ExtentManager.getExtentTest() != null) {
            ExtentManager.getExtentTest().pass(message);
        }
    }

    /**
     * Logs a message with FAIL status in the Extent report.
     * Also attaches a screenshot if configured to do so for failed steps.
     *
     * @param message The message to log (e.g., an error message or assertion failure).
     * @see #addScreenshotToReport(String)
     * @see ConfigProperties#FAILEDSTEPSSCREENSHOT
     */
    public static void fail(String message) {
        if (ExtentManager.getExtentTest() != null) {
            ExtentManager.getExtentTest().fail(message);
            // Optionally attach screenshot on failure - this logic might be better in a listener
            // if (PropertyUtils.getValue(ConfigProperties.FAILEDSTEPSSCREENSHOT).equalsIgnoreCase("yes")) {
            //     addScreenshotToReport("Screenshot on failure"); // Assuming a screenshot utility exists
            // }
        }
    }

    /**
     * Logs a message with SKIP status in the Extent report.
     *
     * @param message The message to log (e.g., reason for skipping).
     */
    public static void skip(String message) {
        if (ExtentManager.getExtentTest() != null) {
            ExtentManager.getExtentTest().skip(message);
        }
    }

    /**
     * Logs a message with INFO status in the Extent report.
     *
     * @param message The informational message to log.
     */
    public static void info(String message) {
        if (ExtentManager.getExtentTest() != null) {
            ExtentManager.getExtentTest().info(message);
        }
    }

    /**
     * Logs a message with WARN status in the Extent report.
     *
     * @param message The warning message to log.
     */
    public static void warn(String message) {
        if (ExtentManager.getExtentTest() != null) {
            ExtentManager.getExtentTest().warning(message); // Corrected method name to .warning()
        }
    }

    /**
     * Logs the details of an API request to the Extent report.
     * This includes the request method, URI, headers, and body (if present and loggable).
     *
     * @param requestSpecification The {@link RequestSpecification} of the API request.
     */
    public static void logRequestToReport(RequestSpecification requestSpecification) {
        if (ExtentManager.getExtentTest() != null && requestSpecification != null) {
            QueryableRequestSpecification queryableRequest = SpecificationQuerier.query(requestSpecification);
            ExtentManager.getExtentTest().info("Request Details:");

            String requestDetails = "<details><summary><b>Click to view Request Details</b></summary>"
                + "<pre>"
                + "Method: " + queryableRequest.getMethod() + "<br>"
                + "URI: " + queryableRequest.getURI() + "<br>"
                + "Headers: " + formatHeaders(queryableRequest.getHeaders()) + "<br>"
                + (queryableRequest.getBody() != null ? "Body: <br>" + queryableRequest.getBody() : "Body: Not available or empty")
                + "</pre>"
                + "</details>";
            ExtentManager.getExtentTest().info(requestDetails);
        }
    }

    /**
     * Logs the details of an API response to the Extent report.
     * This includes the status code, headers, and the response body (pretty printed if JSON/XML).
     *
     * @param response The {@link Response} object from the API call.
     */
    public static void logResponseToReport(Response response) {
        if (ExtentManager.getExtentTest() != null && response != null) {
             ExtentManager.getExtentTest().info("Response Details:");
             String responseDetails = "<details><summary><b>Click to view Response Details</b> (Status: " + response.getStatusCode() + ") </summary>"
                + "<pre>"
                + "Status Code: " + response.getStatusCode() + "<br>"
                + "Response Time (ms): " + response.getTime() + "<br>"
                + "Headers: " + formatHeaders(response.getHeaders()) + "<br>"
                + "Body: <br>" + response.getBody().asPrettyString() // Assumes body is printable
                + "</pre>"
                + "</details>";
            ExtentManager.getExtentTest().info(responseDetails);
        }
    }

    /**
     * Logs a pretty-printed JSON string to the Extent report, typically for API responses.
     * The JSON is wrapped in {@code <pre>} tags for formatting.
     *
     * @param jsonResponse The {@link Response} object containing the JSON.
     */
    public static void logPrettyJsonResponseToReport(Response jsonResponse) {
        if (ExtentManager.getExtentTest() != null && jsonResponse != null) {
            try {
                String prettyJson = jsonResponse.getBody().asPrettyString();
                 String logMessage = "<details><summary><b>Click to view JSON Response Body</b></summary>"
                                 + "<pre>" + prettyJson + "</pre>"
                                 + "</details>";
                ExtentManager.getExtentTest().info(logMessage);
            } catch (Exception e) {
                ExtentManager.getExtentTest().info("Could not log JSON response: " + e.getMessage());
                ExtentManager.getExtentTest().info("Raw Response Body: " + jsonResponse.getBody().asString());
            }
        }
    }

    /**
     * Logs a pretty-printed XML string to the Extent report.
     * The XML is wrapped in {@code <pre>} tags for formatting.
     * (Actual pretty printing of XML might require an external library if not directly supported by RestAssured asPrettyString for XML)
     *
     * @param xmlResponse The {@link Response} object containing the XML.
     */
    public static void logPrettyXmlResponseToReport(Response xmlResponse) {
        if (ExtentManager.getExtentTest() != null && xmlResponse != null) {
            try {
                // RestAssured asPrettyString() works well for JSON, XML might need specific handling for "pretty"
                String prettyXml = xmlResponse.getBody().asPrettyString(); // Check if this provides good XML formatting
                String logMessage = "<details><summary><b>Click to view XML Response Body</b></summary>"
                                 + "<pre>" + escapeHtml(prettyXml) + "</pre>" // Escape HTML characters in XML content
                                 + "</details>";
                ExtentManager.getExtentTest().info(logMessage);
            } catch (Exception e) {
                 ExtentManager.getExtentTest().info("Could not log XML response: " + e.getMessage());
                 ExtentManager.getExtentTest().info("Raw Response Body: " + xmlResponse.getBody().asString());
            }
        }
    }

    /**
     * Formats a list of {@link Header} objects into a string for logging.
     *
     * @param headers The {@link Headers} object from a request or response.
     * @return A string representation of the headers, with each header on a new line.
     *         Returns "No Headers" if the headers object is null or empty.
     */
    private static String formatHeaders(Headers headers) {
        if (headers == null || !headers.exist()) {
            return "No Headers";
        }
        StringBuilder formattedHeaders = new StringBuilder();
        for (Header header : headers) {
            formattedHeaders.append(header.getName()).append(": ").append(header.getValue()).append("<br>");
        }
        return formattedHeaders.toString();
    }

    /**
     * Attaches a screenshot to the Extent report.
     * The screenshot is captured using a utility (not implemented here, assumed to be available e.g., SeleniumUtils.getBase64Image())
     * and embedded as a base64 string.
     *
     * @param message A descriptive message or title for the screenshot.
     * @deprecated This method relies on an external screenshot utility (e.g., SeleniumUtils) which is not part of this class.
     *             Ensure such a utility is available and correctly captures screenshots.
     *             Consider moving screenshot logic to a dedicated screenshot utility class or listener.
     */
    @Deprecated
    public static void addScreenshotToReport(String message) {
        if (ExtentManager.getExtentTest() != null) {
            // String base64Image = SeleniumUtils.getBase64Image(); // Placeholder for actual screenshot capture
            // if (base64Image != null && !base64Image.isEmpty()) {
            //     ExtentManager.getExtentTest().info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
            // } else {
            //     ExtentManager.getExtentTest().info(message + " (Screenshot not available)");
            // }
            ExtentManager.getExtentTest().info(message + " (Screenshot capture logic not implemented in ExtentLogger)");
        }
    }

    /**
     * Escapes HTML special characters in a string to prevent them from being interpreted as HTML.
     * @param text The text to escape.
     * @return The escaped text.
     */
    private static String escapeHtml(String text) {
        if (text == null) return null;
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
