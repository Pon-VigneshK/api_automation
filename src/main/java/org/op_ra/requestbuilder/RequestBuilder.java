package org.op_ra.requestbuilder;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.utils.PropertyUtils;
import org.op_ra.utils.RandomUtils;
import org.op_ra.utils.TestMetrics;

import static io.restassured.RestAssured.given;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;
import static org.op_ra.utils.PropertyUtils.getValue;

/**
 * {@code RequestBuilder} is a class that provides methods for building and sending API requests
 * using REST Assured. This class simplifies the process of creating various types of HTTP requests,
 * such as GET, POST, PUT, DELETE, PATCH, and HEAD.
 *
 * <p>This class handles authentication, headers, and logging for each request. It also records
 * test metrics, such as response time and status code, for reporting purposes.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class are static and can be called directly to perform API requests.</p>
 *
 * <pre>{@code
 * // Sending a GET request
 * Response response = RequestBuilder.requestGetCall("/users/123");
 *
 * // Sending a POST request
 * String requestBody = "{\"name\":\"John Doe\"}";
 * Response response = RequestBuilder.requestPostCall("/users", requestBody);
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class RequestBuilder {

    /**
     * Sends a GET request to the specified endpoint.
     *
     * @param endpoint The endpoint to send the GET request to.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestGetCall(String endpoint) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .when().get(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a GET request with an invalid token to the specified endpoint.
     *
     * @param endpoint The endpoint to send the GET request to.
     * @return {@code true} if the status code is 401, {@code false} otherwise.
     */
    public static boolean requestGetCallUsingInvalidToken(String endpoint) {
        Response response = given()
                .auth()
                .oauth2(PropertyUtils.getValue(ConfigProperties.APIKEY))
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

    /**
     * Sends a GET request without any authentication to the specified endpoint.
     *
     * @param endpoint The endpoint to send the GET request to.
     * @return {@code true} if the status code is 401, {@code false} otherwise.
     */
    public static boolean requestGetCallWithoutAuth(String endpoint) {
        Response response = given()
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

    /**
     * Sends a POST request with an invalid token to the specified endpoint.
     *
     * @param endpoint The endpoint to send the POST request to.
     * @param requestBody The request body to be sent with the request.
     * @return {@code true} if the status code is 401, {@code false} otherwise.
     */
    public static boolean requestPostCallUsingInvalidToken(String endpoint, String requestBody) {
        Response response = given()
                .auth()
                .oauth2(PropertyUtils.getValue(ConfigProperties.APIKEY))
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

    /**
     * Sends a GET request with an invalid header to the specified endpoint.
     *
     * @param endpoint The endpoint to send the GET request to.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestGetCallUsingInvalidHeader(String endpoint) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", RandomUtils.getRandomAlphabets(8))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response;
    }

    /**
     * Sends a GET request to an invalid path.
     *
     * @param endpoint The invalid endpoint to send the GET request to.
     * @return {@code true} if the status code is 404, {@code false} otherwise.
     */
    public static boolean requestGetCallInvalidPath(String endpoint) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 404;
    }

    /**
     * Sends a GET request that is expected to return an empty body.
     *
     * @param endpoint The endpoint to send the GET request to.
     * @return {@code true} if the status code is 200 and the body is empty, {@code false} otherwise.
     */
    public static boolean requestInvalidReadGetCall(String endpoint) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        boolean isStatus200 = response.getStatusCode() == 200;

        boolean isBodyEmpty = false;
        try {
            isBodyEmpty = response.jsonPath().getList("content").isEmpty()
                    && response.jsonPath().getInt("totalElements") == 0
                    && response.jsonPath().getBoolean("empty");
        } catch (Exception e) {
            log(INFO, "Error parsing response body: " + e.getMessage());
            ExtentLogger.log("Error parsing response body: " + e.getMessage());
        }

        return isStatus200 && isBodyEmpty;
    }

    /**
     * Sends a GET request that is expected to return a 400 status code.
     *
     * @param endpoint The endpoint to send the GET request to.
     * @return {@code true} if the status code is 400, {@code false} otherwise.
     */
    public static boolean requestInvalidReadGetCallByString(String endpoint) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 400;
    }

    /**
     * Sends a POST request with an invalid header to the specified endpoint.
     *
     * @param endpoint The endpoint to send the POST request to.
     * @param resquestBody The request body to be sent with the request.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestPostCallUsingInvalidHeader(String endpoint, String resquestBody) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", RandomUtils.getRandomAlphabets(8))
                .header("Content-Type", "application/json")
                .body(resquestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response;
    }

    /**
     * Sends a POST request to the specified endpoint.
     *
     * @param endpoint The endpoint to send the POST request to.
     * @param requestBody The request body to be sent with the request.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestPostCall(String endpoint, String requestBody) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a PUT request with an invalid token to the specified endpoint.
     *
     * @param endpoint The endpoint to send the PUT request to.
     * @param requestBody The request body to be sent with the request.
     * @return {@code true} if the status code is 401, {@code false} otherwise.
     */
    public static boolean requestPutCallUsingInvalidToken(String endpoint, String requestBody) {
        Response response = given()
                .auth()
                .oauth2(PropertyUtils.getValue(ConfigProperties.APIKEY))
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();

        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

    /**
     * Sends a POST request with a practice ID header to the specified endpoint.
     *
     * @param endpoint The endpoint to send the POST request to.
     * @param requestBody The request body to be sent with the request.
     * @param practiceId The practice ID to be sent in the "x-practiceid" header.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestPostCallWithPracticeId(String endpoint, String requestBody, String practiceId) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .header("x-practiceid", practiceId)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a PUT request to the specified endpoint.
     *
     * @param endpoint The endpoint to send the PUT request to.
     * @param requestBody The request body to be sent with the request.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestPutCall(String endpoint, String requestBody) {

        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().put(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a DELETE request to the specified endpoint.
     *
     * @param endpoint The endpoint to send the DELETE request to.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestDeleteCall(String endpoint) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .when().delete(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a PATCH request to the specified endpoint.
     *
     * @param endpoint The endpoint to send the PATCH request to.
     * @param requestBody The request body to be sent with the request.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestPatchCall(String endpoint, String requestBody) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().patch(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

    /**
     * Sends a HEAD request to the specified endpoint.
     *
     * @param endpoint The endpoint to send the HEAD request to.
     * @return The {@link Response} object from the API call.
     */
    public static Response requestHeadCall(String endpoint) {
        Response response = given()
                .auth()
                .oauth2(AuthorizationManager.getCurrentSessionToken())
                .header("x-organizationid", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER))
                .when().head(endpoint)
                .then()
                .extract()
                .response();
        log(INFO, "Received response with status code: " + response.getStatusCode());
        ExtentLogger.log("Received response with status code: " + response.getStatusCode());
        log(INFO, "Response Time : " + response.getTime() + " (ms)");
        ExtentLogger.log("Response Time : " + response.getTime() + " (ms)");

        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        return response;
    }
}
