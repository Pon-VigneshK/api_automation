package org.op_ra.requestbuilder;

import io.restassured.response.Response;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.utils.PropertyUtils;

import static io.restassured.RestAssured.given;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;
import static org.op_ra.utils.PropertyUtils.getValue;

public class ApiRequestBuilder {

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

    public static boolean requestGetCallUsingInvalidToken(String endpoint) {
        Response response = given()
                .auth().preemptive().basic("Test", "Test12")
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

        return response.getStatusCode() == 401;
    }

    public static Response requestGetCallUsingInvalidHeader(String endpoint, String service) {
        String username = "";
        String password = "";

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
                break;
        }

        Response response = given()
                .auth().preemptive().basic(username, password)
                .header("X-Custom-Invalid-Header", "unexpectedValue")
                .header("Content-Type", "application/unknown")
                .when()
                .put(endpoint)
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
