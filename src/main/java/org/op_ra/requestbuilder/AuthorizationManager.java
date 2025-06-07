package org.op_ra.requestbuilder;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.enums.LogType;
import org.op_ra.utils.PropertyUtils;

import static org.op_ra.reports.FrameworkLogger.log;

public final class AuthorizationManager {

    private AuthorizationManager() {
    }


    public static String getCurrentSessionToken() {
        try {
            // Disable SSL verification (use it cautiously)
            RestAssured.useRelaxedHTTPSValidation();
            Response response = RestAssured.given()
                    .auth().preemptive().basic(PropertyUtils.getValue(ConfigProperties.CLIENT_ID), PropertyUtils.getValue(ConfigProperties.CLIENT_SECRET))
                    .contentType("application/x-www-form-urlencoded")
                    .formParams("grant_type", "client_credentials")
                    .formParam("scope",PropertyUtils.getValue(ConfigProperties.SCOPE))
                    .when()
                    .post(PropertyUtils.getValue(ConfigProperties.ACCESS_TOKEN_URL));
            if (response.getStatusCode() == 200) {
                String accessToken = response.jsonPath().getString("access_token");
                System.setProperty("auth_token", accessToken);
                return accessToken;
            } else {
                log(LogType.ERROR, "Authentication failed with status code: " + response.getStatusCode());
                return null;
            }
        } catch (RuntimeException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return null;
        }

    }
}




