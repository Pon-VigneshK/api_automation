package org.op_ra.requestbuilder;

import io.restassured.specification.RequestSpecification;
// Assuming other necessary imports like PropertyUtils for credentials, ConfigProperties enum.
import org.op_ra.enums.ConfigProperties;
import org.op_ra.utils.PropertyUtils;

/**
 * Manages different types of authorization for API requests.
 * This class provides static methods to apply common authentication mechanisms
 * (e.g., Basic Auth, OAuth2, API Key) to a {@link RequestSpecification}.
 * <p>
 * The goal is to centralize authorization logic, making it easy to apply consistent
 * authentication across various API requests. Credentials or tokens required for
 * authorization are typically fetched from configuration files via {@link PropertyUtils}.
 * </p>
 * Example Usage:
 * <pre>{@code
 * RequestSpecification request = RestAssured.given();
 * AuthorizationManager.setBasicAuth(request, ConfigProperties.API_USERNAME, ConfigProperties.API_PASSWORD);
 * // ... then build the rest of the request and send it.
 *
 * RequestSpecification requestWithToken = RestAssured.given();
 * AuthorizationManager.setBearerTokenAuth(requestWithToken, "your_bearer_token_here");
 * }</pre>
 */
public final class AuthorizationManager {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AuthorizationManager() {
        // Private constructor
    }

    /**
     * Applies Basic Authentication to the given {@link RequestSpecification}.
     * Retrieves username and password from configuration based on the provided {@link ConfigProperties} keys.
     *
     * @param requestSpec The {@link RequestSpecification} to which Basic Auth will be applied.
     * @param usernameKey The {@link ConfigProperties} key for the username.
     * @param passwordKey The {@link ConfigProperties} key for the password.
     * @return The same {@link RequestSpecification} with Basic Authentication configured.
     * @throws RuntimeException if username or password properties are not found in configuration.
     */
    public static RequestSpecification setBasicAuth(RequestSpecification requestSpec,
                                                    ConfigProperties usernameKey,
                                                    ConfigProperties passwordKey) {
        String username = PropertyUtils.getValue(usernameKey);
        String password = PropertyUtils.getValue(passwordKey);

        if (username == null || password == null) {
            throw new RuntimeException("Username or Password not found in properties for Basic Auth. UsernameKey: " + usernameKey + ", PasswordKey: " + passwordKey);
        }
        return requestSpec.auth().preemptive().basic(username, password);
    }

    /**
     * Applies Basic Authentication to the given {@link RequestSpecification} using explicit username and password strings.
     *
     * @param requestSpec The {@link RequestSpecification} to which Basic Auth will be applied.
     * @param username    The username for Basic Authentication.
     * @param password    The password for Basic Authentication.
     * @return The same {@link RequestSpecification} with Basic Authentication configured.
     */
    public static RequestSpecification setBasicAuth(RequestSpecification requestSpec,
                                                    String username,
                                                    String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or Password cannot be null for Basic Auth.");
        }
        return requestSpec.auth().preemptive().basic(username, password);
    }

    /**
     * Applies Bearer Token Authentication to the given {@link RequestSpecification}.
     *
     * @param requestSpec The {@link RequestSpecification} to which Bearer Token Auth will be applied.
     * @param token       The Bearer token string.
     * @return The same {@link RequestSpecification} with Bearer Token Authentication configured.
     */
    public static RequestSpecification setBearerTokenAuth(RequestSpecification requestSpec, String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Bearer token cannot be null or empty.");
        }
        return requestSpec.header("Authorization", "Bearer " + token);
    }

    /**
     * Applies API Key Authentication to the given {@link RequestSpecification}.
     * The API key is added as a header.
     *
     * @param requestSpec The {@link RequestSpecification} to which API Key Auth will be applied.
     * @param apiKeyHeaderName The name of the header used for the API key (e.g., "X-API-KEY", "Authorization").
     * @param apiKeyValue      The value of the API key.
     * @return The same {@link RequestSpecification} with API Key Authentication configured.
     */
    public static RequestSpecification setApiKeyAuth(RequestSpecification requestSpec, String apiKeyHeaderName, String apiKeyValue) {
        if (apiKeyHeaderName == null || apiKeyHeaderName.trim().isEmpty() || apiKeyValue == null || apiKeyValue.trim().isEmpty()) {
            throw new IllegalArgumentException("API Key header name and value cannot be null or empty.");
        }
        return requestSpec.header(apiKeyHeaderName, apiKeyValue);
    }

    /**
     * Applies API Key Authentication to the given {@link RequestSpecification}.
     * The API key is added as a query parameter.
     *
     * @param requestSpec The {@link RequestSpecification} to which API Key Auth will be applied.
     * @param apiKeyQueryParamName The name of the query parameter for the API key (e.g., "api_key").
     * @param apiKeyValue          The value of the API key.
     * @return The same {@link RequestSpecification} with API Key Authentication configured.
     */
    public static RequestSpecification setApiKeyAuthAsQueryParam(RequestSpecification requestSpec, String apiKeyQueryParamName, String apiKeyValue) {
        if (apiKeyQueryParamName == null || apiKeyQueryParamName.trim().isEmpty() || apiKeyValue == null || apiKeyValue.trim().isEmpty()) {
            throw new IllegalArgumentException("API Key query parameter name and value cannot be null or empty.");
        }
        return requestSpec.queryParam(apiKeyQueryParamName, apiKeyValue);
    }

    // TODO: Add methods for other common authorization types like OAuth2 (more complex, might involve token fetching)
    // Example for OAuth2 (conceptual - would require a token management strategy):
    // public static RequestSpecification setOAuth2(RequestSpecification requestSpec, String token) {
    //     if (token == null || token.trim().isEmpty()) {
    //         // Fetch token logic here if not provided, or throw error
    //         throw new IllegalArgumentException("OAuth2 token cannot be null or empty.");
    //     }
    //     return requestSpec.auth().oauth2(token);
    // }
}
