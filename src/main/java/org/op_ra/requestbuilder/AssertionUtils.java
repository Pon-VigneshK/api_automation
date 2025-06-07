package org.op_ra.requestbuilder;
import io.restassured.response.Response;
import org.op_ra.enums.LogType;
import org.op_ra.reports.ExtentLogger;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import static org.op_ra.reports.FrameworkLogger.log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.lessThan;
import static org.op_ra.constants.FrameworkConstants.getMaxLimit;

public class AssertionUtils {


    public static boolean findValues(String targetKey, String expectedValue, String jsonStr) {
        log(LogType.INFO, String.format("Searching for key: '%s' with expected value: '%s'", targetKey, expectedValue));
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {});
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

    public static boolean validateValueInXml(Response response, String objectName, String attributeName, String expectedValue) {
        response.then().statusCode(200);
        response.then().time(lessThan(getMaxLimit()));
        String xml = response.getBody().asString();
        ExtentLogger.info(String.format("Validating XML for tag='%s', attribute='%s', expectedValue='%s'", objectName, attributeName, expectedValue));
        log(LogType.INFO, String.format("Validating XML for tag='%s', attribute='%s', expectedValue='%s'", objectName, attributeName, expectedValue));

        try {
            Document document = getDocument(xml);
            NodeList nodes = document.getElementsByTagName(objectName);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String actualValue = getElementValue(element, attributeName);
                if (!actualValue.toLowerCase().contains(expectedValue.toLowerCase())) {
                    ExtentLogger.fail(String.format("Expected value '%s' not found in tag '%s'", expectedValue, objectName));
                    log(LogType.ERROR, String.format("Expected value '%s' not found in tag '%s'", expectedValue, objectName));
                    return false;
                }
            }

            ExtentLogger.pass("All matching elements contain the expected value.");
            log(LogType.INFO, "All matching elements contain the expected value.");
            return true;

        } catch (Exception e) {
            ExtentLogger.fail("Error parsing XML or validating value: " + e.getMessage());
            log(LogType.ERROR, "Error parsing XML or validating value: " + e.getMessage());
            return false;
        }
    }

    public static boolean isXmlElementListEmpty(Response response, String listElementTag) {
        response.then().statusCode(200);
        response.then().time(lessThan(getMaxLimit()));
        String xml = response.getBody().asString();
        ExtentLogger.info(String.format("Checking if list element '%s' is empty in XML response", listElementTag));
        log(LogType.INFO, String.format("Checking if list element '%s' is empty in XML response", listElementTag));

        try {
            Document document = getDocument(xml);
            NodeList list = document.getElementsByTagName(listElementTag);

            boolean result = list.getLength() > 0 && !list.item(0).hasChildNodes();
            if (result) {
                ExtentLogger.pass(String.format("Element '%s' list is empty.", listElementTag));
                log(LogType.INFO, String.format("Element '%s' list is empty.", listElementTag));
            } else {
                ExtentLogger.fail(String.format("Element '%s' list is not empty.", listElementTag));
                log(LogType.ERROR, String.format("Element '%s' list is not empty.", listElementTag));
            }

            return result;

        } catch (Exception e) {
            ExtentLogger.fail("Error parsing XML for list element check: " + e.getMessage());
            log(LogType.ERROR, "Error parsing XML for list element check: " + e.getMessage());
            return false;
        }
    }


    private static Document getDocument(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    private static String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }


    public static boolean isResponseBodyEqualTo(Response response, String expectedText) {
        response.then().statusCode(200);
        response.then().time(lessThan(getMaxLimit()));
        String actualBody = response.getBody().asString();

        boolean result = actualBody.trim().equals(expectedText.trim());

        if (result) {
            ExtentLogger.pass("Response body matches the expected text.");
            log(LogType.INFO, "Response body matches the expected text.");
        } else {
            ExtentLogger.fail(String.format("Response body does not match.\nExpected: %s\nActual: %s", expectedText, actualBody));
            log(LogType.ERROR, String.format("Response body does not match.\nExpected: %s\nActual: %s", expectedText, actualBody));
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


