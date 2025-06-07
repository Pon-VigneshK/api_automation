package org.op_ra.requestbuilder;

import io.restassured.response.Response;
import org.testng.Assert; // Using TestNG Assertions

/**
 * Utility class for common API response assertions.
 * This class provides static methods to perform various checks on the {@link Response} object,
 * such as verifying status codes, headers, and body content.
 * <p>
 * Using this class helps in creating more readable and maintainable assertion logic in test scripts.
 * It standardizes common assertions and can also integrate with {@link org.op_ra.reports.ExtentLogger}
 * to log assertion details.
 * </p>
 * Example usage:
 * <pre>{@code
 * Response response = // ... API call ...
 * AssertionUtils.assertStatusCode(response, 200);
 * AssertionUtils.assertContentType(response, "application/json");
 * AssertionUtils.assertResponseBodyContains(response, "expectedValue");
 * }</pre>
 */
public final class AssertionUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AssertionUtils() {
        // Private constructor
    }

    /**
     * Asserts that the response status code matches the expected status code.
     * Logs the assertion result to ExtentReports.
     *
     * @param response           The {@link Response} object from the API call.
     * @param expectedStatusCode The expected HTTP status code (e.g., 200, 201, 404).
     */
    public static void assertStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        try {
            Assert.assertEquals(actualStatusCode, expectedStatusCode,
                    "Status code mismatch. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
            org.op_ra.reports.ExtentLogger.pass("Assertion PASSED: Status code is " + expectedStatusCode);
        } catch (AssertionError e) {
            org.op_ra.reports.ExtentLogger.fail("Assertion FAILED: Status code mismatch. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
            org.op_ra.reports.ExtentLogger.fail("Response Body: <pre>" + response.getBody().asPrettyString() + "</pre>");
            throw e; // Re-throw the assertion error to mark the test as failed
        }
    }

    /**
     * Asserts that the response Content-Type header matches the expected content type.
     *
     * @param response              The {@link Response} object.
     * @param expectedContentType   The expected content type string (e.g., "application/json", "text/xml; charset=utf-8").
     *                              The assertion is typically case-insensitive for the main type but sensitive for parameters like charset.
     */
    public static void assertContentType(Response response, String expectedContentType) {
        String actualContentType = response.getContentType();
        try {
            // Using startsWith for flexibility, as Content-Type can have charset, etc.
            // For exact match: Assert.assertEquals(actualContentType, expectedContentType, "Content-Type mismatch.");
            Assert.assertTrue(actualContentType.toLowerCase().startsWith(expectedContentType.toLowerCase()),
                    "Content-Type mismatch. Expected to start with: '" + expectedContentType + "', Actual: '" + actualContentType + "'");
            org.op_ra.reports.ExtentLogger.pass("Assertion PASSED: Content-Type starts with '" + expectedContentType + "'. Actual: '" + actualContentType + "'");
        } catch (AssertionError e) {
            org.op_ra.reports.ExtentLogger.fail("Assertion FAILED: Content-Type mismatch. Expected to start with: '" + expectedContentType + "', Actual: '" + actualContentType + "'");
            throw e;
        }
    }

    /**
     * Asserts that the response body (converted to a string) contains the expected text.
     *
     * @param response     The {@link Response} object.
     * @param expectedText The text expected to be present in the response body.
     */
    public static void assertResponseBodyContains(Response response, String expectedText) {
        String responseBody = response.getBody().asString();
        try {
            Assert.assertTrue(responseBody.contains(expectedText),
                    "Response body does not contain the expected text: '" + expectedText + "'");
            org.op_ra.reports.ExtentLogger.pass("Assertion PASSED: Response body contains text: '" + expectedText + "'");
        } catch (AssertionError e) {
            org.op_ra.reports.ExtentLogger.fail("Assertion FAILED: Response body does not contain the expected text: '" + expectedText + "'");
            org.op_ra.reports.ExtentLogger.fail("Response Body: <pre>" + response.getBody().asPrettyString() + "</pre>");
            throw e;
        }
    }

    /**
     * Asserts that a specific JSON path in the response body matches an expected value.
     * Uses RestAssured'''s JsonPath for evaluation.
     *
     * @param response     The {@link Response} object.
     * @param jsonPath     The JsonPath expression (e.g., "data.id", "user.name").
     * @param expectedValue The expected value at the given JsonPath. Can be String, Integer, Boolean, etc.
     */
    public static void assertJsonPathValue(Response response, String jsonPath, Object expectedValue) {
        Object actualValue = null;
        try {
            actualValue = response.jsonPath().get(jsonPath);
            Assert.assertEquals(actualValue, expectedValue,
                    "JSONPath value mismatch for path: '" + jsonPath + "'. Expected: '" + expectedValue + "', Actual: '" + actualValue + "'");
            org.op_ra.reports.ExtentLogger.pass("Assertion PASSED: JSONPath '" + jsonPath + "' has value: '" + expectedValue + "'");
        } catch (AssertionError e) {
            org.op_ra.reports.ExtentLogger.fail("Assertion FAILED: JSONPath value mismatch for path: '" + jsonPath + "'. Expected: '" + expectedValue + "', Actual: '" + actualValue + "'");
            org.op_ra.reports.ExtentLogger.fail("Response Body: <pre>" + response.getBody().asPrettyString() + "</pre>");
            throw e;
        } catch (Exception ex) { // Catch potential exceptions from jsonPath().get() if path is invalid or body not JSON
             org.op_ra.reports.ExtentLogger.fail("Assertion FAILED: Error evaluating JSONPath '" + jsonPath + "'. Error: " + ex.getMessage());
             org.op_ra.reports.ExtentLogger.fail("Response Body: <pre>" + response.getBody().asPrettyString() + "</pre>");
            throw new AssertionError("Error evaluating JSONPath '" + jsonPath + "': " + ex.getMessage(), ex);
        }
    }

    /**
     * Asserts that a specific header in the response exists and matches an expected value.
     *
     * @param response      The {@link Response} object.
     * @param headerName    The name of the header to check.
     * @param expectedValue The expected value of the header.
     */
    public static void assertHeaderValue(Response response, String headerName, String expectedValue) {
        String actualHeaderValue = response.getHeader(headerName);
        try {
            Assert.assertNotNull(actualHeaderValue, "Header '" + headerName + "' does not exist.");
            Assert.assertEquals(actualHeaderValue, expectedValue,
                    "Header '" + headerName + "' value mismatch. Expected: '" + expectedValue + "', Actual: '" + actualHeaderValue + "'");
            org.op_ra.reports.ExtentLogger.pass("Assertion PASSED: Header '" + headerName + "' has value: '" + expectedValue + "'");
        } catch (AssertionError e) {
            org.op_ra.reports.ExtentLogger.fail("Assertion FAILED: Header '" + headerName + "' value mismatch. Expected: '" + expectedValue + "', Actual: '" + actualHeaderValue + "' (Header might be missing or value differs)");
            throw e;
        }
    }

    // Add more assertion methods as needed, for example:
    // - Asserting response time is within a limit
    // - Asserting JSON schema validity
    // - Asserting array sizes or specific elements in JSON arrays
}
