package org.op_ra.requestbuilder;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.utils.StringDecoder;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.*;
import static org.op_ra.enums.LogType.ERROR;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;
import static org.op_ra.utils.PropertyUtils.getValue;

/**
 * {@code AuthorizationManager} is a final class that manages authorization for the application
 * using various authentication methods. It provides methods to retrieve an access token for
 * OAuth2 authentication.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The {@code getCurrentSessionToken} method can be called to obtain an OAuth2 access token.</p>
 *
 * <pre>{@code
 * String accessToken = AuthorizationManager.getCurrentSessionToken();
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public final class AuthorizationManager {

    private AuthorizationManager() {
    }

    /**
     * Obtains an OAuth2 access token using the client credentials grant type. It sends a POST
     * request to the access token URL with the client ID and client secret.
     *
     * <p>This method uses relaxed HTTPS validation, so it should be used with caution in a
     * production environment.</p>
     *
     * @return The access token as a string, or {@code null} if authentication fails.
     */
    public static String getCurrentSessionToken() {
        try {
            RestAssured.useRelaxedHTTPSValidation();
            Map<String, String> body = new HashMap<>();
            body.put("grant_type", "client_credentials");
            body.put("audience", getValue(AUDIENCE));
            String accessToken = RestAssured.given()
                    .auth().preemptive().basic(StringDecoder.decodeBase64UTF8(getValue(CLIENTID)), StringDecoder.decodeBase64UTF8(getValue(CLIENTSECRET)))
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post(getValue(ACCESSTOKENURL)).then().statusCode(200).extract().jsonPath().get("access_token");
            String apiCallHeaders = accessToken;
            ExtentLogger.log("User authenticated successfully, with the client id of: " + getValue(CLIENTID));
            ExtentLogger.log("Auth Token: " + apiCallHeaders);
            log(INFO, "User authenticated successfully, with the client id of: " + getValue(CLIENTID));
            log(INFO, "Auth Token: " + apiCallHeaders);
            System.setProperty("auth_token", apiCallHeaders);
            return apiCallHeaders;
        } catch (RuntimeException e) {
            ExtentLogger.fail("Authentication failed: " + e.getMessage());
            log(ERROR, "Authentication failed: " + e.getMessage());
            return null;
        }
    }
}
