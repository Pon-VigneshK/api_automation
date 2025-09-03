package org.op_ra.enums;

/**
 * {@code ConfigProperties} is an enumeration that defines the keys for the configuration properties file.
 * This enum is used to retrieve configuration values using the {@link org.op_ra.utils.PropertyUtils} class.
 * Using this enum helps prevent typos and ensures that all configuration keys are defined in a single place.
 *
 * <p><b>Usage:</b></p>
 * <p>To get a configuration value, use this enum with {@code PropertyUtils.getValue()}.</p>
 *
 * <pre>{@code
 * String baseUrl = PropertyUtils.getValue(ConfigProperties.BASEURL);
 * }</pre>
 *
 * <p>Whenever a new property is added to the configuration file, a corresponding entry should be added to this enum.</p>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public enum ConfigProperties {

    /**
     * The base URL of the API to be tested.
     */
    BASEURL,

    /**
     * Specifies whether to override existing reports. Accepts "yes" or "no".
     */
    OVERRIDEREPORTS,

    /**
     * Specifies the number of times to retry a failed test case.
     */
    RETRY,

    /**
     * The environment in which the tests are being run (e.g., "QA", "UAT").
     */
    ENV,

    /**
     * The type of authentication to be used (e.g., "oauth2", "basic").
     */
    AUTHTYPE,

    /**
     * The mode in which to run the tests (e.g., "local", "remote").
     */
    RUNMODE,

    /**
     * The URL for obtaining the access token for OAuth2 authentication.
     */
    ACCESSTOKENURL,

    /**
     * The client ID for OAuth2 authentication.
     */
    CLIENTID,

    /**
     * The client secret for OAuth2 authentication.
     */
    CLIENTSECRET,

    /**
     * The scope for OAuth2 authentication.
     */
    SCOPE,

    /**
     * The audience for OAuth2 authentication.
     */
    AUDIENCE,

    /**
     * The prefix for the authorization header (e.g., "Bearer").
     */
    AUTHPREFIX,

    /**
     * The username for basic authentication.
     */
    USERNAME,

    /**
     * The password for basic authentication.
     */
    PASSWORD,

    /**
     * Specifies whether to log the API response. Accepts "true" or "false".
     */
    LOGRESPONSE,

    /**
     * The name of the audit log database.
     */
    AUDITLOGDB,

    /**
     * The API key for authentication.
     */
    APIKEY,

    /**
     * Specifies whether a header is needed for the request. Accepts "true" or "false".
     */
    NEEDHEADER,

    /**
     * The value for the "Accept" header.
     */
    ACCEPTHEADER,

    /**
     * The value for the "Content-Type" header.
     */
    CONTENTTYPEHEADER,

    /**
     * The value for the "Organization-ID" header.
     */
    ORGANIZATIONIDHEADER,

    /**
     * The value for the "Practice-ID" header.
     */
    PRACTICEIDHEADER;

}
