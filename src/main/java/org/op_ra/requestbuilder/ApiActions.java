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

public class ApiActions {

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
