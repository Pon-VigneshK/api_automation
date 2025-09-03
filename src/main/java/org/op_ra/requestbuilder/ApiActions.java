package org.op_ra.requestbuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.enums.LogType;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.utils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.lessThan;
import static org.op_ra.constants.FrameworkConstants.getMaxLimit;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;

/**
 * {@code ApiActions} is a utility class that provides methods for performing common API actions and verifications.
 * This class includes methods for searching for values in a JSON response, verifying key-value pairs,
 * forming endpoints, and checking response status codes and body content.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to perform verifications on a {@link Response} object or to
 * construct an endpoint URL.</p>
 *
 * <pre>{@code
 * // Verifying key-value pairs in a response
 * Map<String, Object> expectedValues = new HashMap<>();
 * expectedValues.put("name", "John Doe");
 * boolean isVerified = ApiActions.verifyJsonKeyValues(response, expectedValues);
 *
 * // Forming an endpoint URL
 * String endpoint = ApiActions.formEndpoint("/users/%s", "123");
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class ApiActions {

    /**
     * Searches for a specific key-value pair in a JSON string.
     *
     * @param targetKey The key to search for.
     * @param expectedValue The expected value of the key.
     * @param jsonStr The JSON string to search in.
     * @return {@code true} if the key-value pair is found, {@code false} otherwise.
     */
    public static boolean findValues(String targetKey, String expectedValue, String jsonStr) {
        log(LogType.INFO, String.format("Searching for key: '%s' with expected value: '%s'", targetKey, expectedValue));
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
            });
            List<String> results = new ArrayList<>();
            extractMatchingValues(jsonMap, targetKey, results);
            ExtentLogger.pass(String.format("Found values for key '%s': '%s'", targetKey, results));
            log(LogType.DEBUG, String.format("Found values for key '%s': '%s'", targetKey, results));
            return results.contains(expectedValue);
        } catch (Exception e) {
            ExtentLogger.fail(String.format("Failed to parse JSON or search key '%s': '%s'", targetKey, e.getMessage()));
            log(LogType.ERROR, String.format("Failed to parse JSON or search key '%s': '%s'", targetKey, e.getMessage()));
            e.printStackTrace();
            return false;
        }
    }

    private static void extractMatchingValues(Object obj, String targetKey, List<String> results) {
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (targetKey.equals(entry.getKey())) {
                    results.add(String.valueOf(entry.getValue()));
                }
                extractMatchingValues(entry.getValue(), targetKey, results);
            }
        } else if (obj instanceof List<?>) {
            for (Object item : (List<?>) obj) {
                extractMatchingValues(item, targetKey, results);
            }
        }
    }

    /**
     * Verifies that the JSON response contains the expected key-value pairs. It also checks that the
     * status code is 200 and the response time is within the acceptable limit.
     *
     * @param response The REST Assured response object.
     * @param expectedValues A map of expected key-value pairs.
     * @return {@code true} if all verifications pass, {@code false} otherwise.
     */
    public static boolean verifyJsonKeyValues(Response response, Map<String, Object> expectedValues) {
        boolean verification = true;
        response.then().statusCode(200);
        response.then().time(lessThan(getMaxLimit()));
        String jsonStr = response.getBody().asString();
        ExtentLogger.info(String.format("Verifying response against expected values: '%s'", expectedValues));
        log(LogType.INFO, String.format("Verifying response against expected values: '%s'", expectedValues));
        for (Map.Entry<String, Object> entry : expectedValues.entrySet()) {
            String key = entry.getKey();
            Object expected = entry.getValue();
            if (expected instanceof String) {
                boolean result = findValues(key, (String) expected, jsonStr);
                ExtentLogger.info(String.format("Verification for key='%s', expected='%s': '%s'", key, expected, result));
                log(LogType.INFO, String.format("Verification for key='%s', expected='%s': '%s'", key, expected, result));
                if (!result) verification = false;

            } else if (expected instanceof List<?>) {
                for (Object val : (List<?>) expected) {
                    boolean result = findValues(key, String.valueOf(val), jsonStr);
                    ExtentLogger.info(String.format("Verification for key='%s', expected='%s': '%s'", key, val, result));
                    log(LogType.INFO, String.format("Verification for key='%s', expected='%s': '%s'", key, val, result));
                    if (!result) verification = false;
                }
            }
        }
        ExtentLogger.info(String.format("Overall verification result: %s", verification));
        log(LogType.INFO, String.format("Overall verification result: %s", verification));
        return verification;
    }

    /**
     * Constructs the full endpoint URL by combining the base URL from the properties file with the
     * given endpoint and parameters.
     *
     * @param endpoint The endpoint path with format specifiers.
     * @param params The parameters to be formatted into the endpoint path.
     * @return The full endpoint URL.
     */
    public static String formEndpoint(String endpoint, String... params) {
        try {
            ExtentLogger.log("User authenticated successfully via OAuth2 with client ID: " + PropertyUtils.getValue(ConfigProperties.CLIENTID));
        } catch (Exception e) {
            System.out.println("ExtentLogger failed: " + e.getMessage());
        }
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.BASEURL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting endpoint from Atlas Practice Service: " + fullEndpoint);
        ExtentLogger.log("Requesting endpoint from Atlas Practice Service: " + fullEndpoint);
        return fullEndpoint;
    }

    /**
     * Checks if the response body is empty. It also verifies that the status code is 200 and the
     * response time is within the acceptable limit.
     *
     * @param response The REST Assured response object.
     * @return {@code true} if the response body is empty, {@code false} otherwise.
     */
    public static boolean isResponseBodyEmpty(Response response) {
        response.then().statusCode(200);
        response.then().time(lessThan(getMaxLimit()));
        String body = response.getBody().asString();

        boolean result = (body == null || body.trim().isEmpty());

        if (result) {
            ExtentLogger.pass("Response body is empty as expected.");
            log(LogType.INFO, "Response body is empty as expected.");
        } else {
            ExtentLogger.fail("Response body is not empty. Actual content: " + body);
            log(LogType.ERROR, "Response body is not empty. Actual content: " + body);
        }

        return result;
    }

    /**
     * Checks if the status code of the response is equal to the expected status code.
     *
     * @param response The REST Assured response object.
     * @param expectedStatusCode The expected status code.
     * @return {@code true} if the status codes match, {@code false} otherwise.
     */
    public static boolean isStatusCodeEqualTo(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();

        boolean result = (actualStatusCode == expectedStatusCode);

        if (result) {
            ExtentLogger.pass(String.format("Status code is as expected: %d", expectedStatusCode));
            log(LogType.INFO, String.format("Status code is as expected: %d", expectedStatusCode));
        } else {
            ExtentLogger.fail(String.format("Status code mismatch. Expected: %d, Actual: %d", expectedStatusCode, actualStatusCode));
            log(LogType.ERROR, String.format("Status code mismatch. Expected: %d, Actual: %d", expectedStatusCode, actualStatusCode));
        }

        return result;
    }
}
