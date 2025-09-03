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

public class RequestBuilder {

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }


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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response;
    }


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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 404;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        // Validate 200 OK with empty body
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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 400;
    }


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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }

        return response.getStatusCode() == 401;
    }

    /**
     * Enhanced POST request method that includes x-practiceid header for endpoints that require it
     *
     * @param endpoint    - API endpoint URL
     * @param requestBody - JSON request body
     * @param practiceId  - Practice ID for x-practiceid header
     * @return Response object
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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        if (getValue(ConfigProperties.LOGRESPONSE).equalsIgnoreCase("Yes")) {
            ExtentLogger.logPrettyJsonResponseToReport(response);
        }
        return response;
    }

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

        // Store metrics for database reporting
        TestMetrics.setHttpStatusCode(response.getStatusCode());
        TestMetrics.setResponseTimeMs(response.getTime());

        return response;
    }

}
