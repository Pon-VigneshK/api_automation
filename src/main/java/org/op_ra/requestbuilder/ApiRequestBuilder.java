package org.op_ra.requestbuilder;

import io.restassured.response.Response;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.utils.PropertyUtils;

import static io.restassured.RestAssured.given;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;
import static org.op_ra.utils.PropertyUtils.getValue;

/**
 * Builds and sends API requests using RestAssured.
 * This class provides methods for various HTTP methods (GET, POST, PUT)
 * and handles authentication, logging, and response extraction for different services.
 */
public class ApiRequestBuilder {

    /**
     * Sends a GET request to the Actor service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response getActorServiceResponse(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_ACTOR_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_ACTOR_PASSWORD))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a GET request to the Chart service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response getChartServiceResponse(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_CHART_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_CHART_PASSWORD))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a GET request to the CHC service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response getChcServiceResponse(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_CHC_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_CHC_PASSWORD))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a GET request to the Document service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response getDocumentServiceResponse(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_DOCUMENT_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_DOCUMENT_PASSWORD))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a GET request to the Erx service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response getErxServiceResponse(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_ERX_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_ERX_PASSWORD))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a GET request to the Lab service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response getLabServiceResponse(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_LAB_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_LAB_PASSWORD))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a GET request to the Job service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response getJobServiceResponse(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_JOB_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_JOB_PASSWORD))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a POST request to the Actor service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response postActorService(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_ACTOR_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_ACTOR_PASSWORD))
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a POST request to the Chart service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response postChartService(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_CHART_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_CHART_PASSWORD))
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a POST request to the CHC service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response postChcService(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_CHART_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_CHART_PASSWORD))
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a POST request to the Document service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response postDocumentService(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_CHART_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_CHART_PASSWORD))
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a POST request to the Erx service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response postErxService(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_CHART_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_CHART_PASSWORD))
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a POST request to the Lab service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response postLabService(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_CHART_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_CHART_PASSWORD))
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a POST request to the Job service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response postJobService(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_JOB_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_JOB_PASSWORD))
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a PUT request to the Job service.
     *
     * @param endpoint The API endpoint to hit.
     * @return The response from the API.
     */
    public static Response putJobService(String endpoint) {
        Response response = given()
                .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.OPEN_JOB_USERNAME), PropertyUtils.getValue(ConfigProperties.OPEN_JOB_PASSWORD))
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");
        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a GET request with an invalid token to test unauthorized access.
     *
     * @param endpoint The API endpoint to hit.
     * @return True if the status code is 401 (Unauthorized), false otherwise.
     */
    public static boolean requestGetCallUsingInvalidToken(String endpoint) {
        Response response = given()
                .auth().preemptive().basic("Test", "Test12") // Invalid credentials
                .when()
                .put(endpoint) // Should this be GET? Assuming PUT based on original code.
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        if (getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

    /**
     * Sends a GET request with an invalid header to test server error handling.
     * The service parameter determines which service credentials to use.
     *
     * @param endpoint The API endpoint to hit.
     * @param service  The target service (e.g., "ERX", "CHC", "CHART").
     * @return The response from the API.
     */
    public static Response requestGetCallUsingInvalidHeader(String endpoint, String service) {
        String username = "";
        String password = "";

        // Determine credentials based on the service
        switch (service.toUpperCase()) {
            case "ERX":
                username = PropertyUtils.getValue(ConfigProperties.OPEN_ERX_USERNAME);
                password = PropertyUtils.getValue(ConfigProperties.OPEN_ERX_PASSWORD);
                break;
            case "CHC":
                username = PropertyUtils.getValue(ConfigProperties.OPEN_CHC_USERNAME);
                password = PropertyUtils.getValue(ConfigProperties.OPEN_CHC_PASSWORD);
                break;
            case "CHART":
                username = PropertyUtils.getValue(ConfigProperties.OPEN_CHART_USERNAME);
                password = PropertyUtils.getValue(ConfigProperties.OPEN_CHART_PASSWORD);
                break;
            case "CODING":
                username = PropertyUtils.getValue(ConfigProperties.OPEN_CODING_USERNAME);
                password = PropertyUtils.getValue(ConfigProperties.OPEN_CODING_PASSWORD);
                break;
            case "ACTOR":
                username = PropertyUtils.getValue(ConfigProperties.OPEN_ACTOR_USERNAME);
                password = PropertyUtils.getValue(ConfigProperties.OPEN_ACTOR_PASSWORD);
                break;
            case "DOCUMENT":
                username = PropertyUtils.getValue(ConfigProperties.OPEN_DOCUMENT_USERNAME);
                password = PropertyUtils.getValue(ConfigProperties.OPEN_DOCUMENT_PASSWORD);
                break;
            case "LAB":
                username = PropertyUtils.getValue(ConfigProperties.OPEN_LAB_USERNAME);
                password = PropertyUtils.getValue(ConfigProperties.OPEN_LAB_PASSWORD);
                break;
            case "JOB":
                username = PropertyUtils.getValue(ConfigProperties.OPEN_JOB_USERNAME);
                password = PropertyUtils.getValue(ConfigProperties.OPEN_JOB_PASSWORD);
                break;
            default:
                log(INFO, "Invalid service type provided: " + service);
                ExtentLogger.log("Invalid service type provided: " + service);
                // Return null or throw an exception for invalid service?
                // For now, it will proceed with empty username/password if service is invalid.
                break;
        }

        Response response = given()
                .auth().preemptive().basic(username, password)
                .header("X-Custom-Invalid-Header", "unexpectedValue")
                .header("Content-Type", "application/unknown")
                .when()
                .put(endpoint) // Should this be GET? Assuming PUT based on original code.
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        if (PropertyUtils.getValue(ConfigProperties.LOG_RESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response;
    }
}
