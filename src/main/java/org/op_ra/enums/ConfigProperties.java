package org.op_ra.enums;

/**
 * Enumeration of configuration property keys used in the framework.
 * These keys correspond to entries in the {@code config.properties} file.
 * Using an enum provides type safety and autocompletion when accessing configuration values
 * via {@link org.op_ra.utils.PropertyUtils#getValue(ConfigProperties)}.
 */
public enum ConfigProperties {
    // General Configuration
    ENV,                        // Current execution environment (e.g., DEV, QA, PROD)
    RUNMANAGER,                 // Manages which set of tests to run (e.g., Smoke, Regression)
    SERVICE_NAME,             // Name of the service under test, used in reporting

    // Base URLs for different services or environments
    BASE_URL,                   // A generic base URL, might be overridden by specific service URLs
    OPEN_ACTOR_BASE_URL,
    OPEN_CHART_BASE_URL,
    OPEN_CHC_BASE_URL,
    OPEN_DOCUMENT_BASE_URL,
    OPEN_ERX_BASE_URL,
    OPEN_LAB_BASE_URL,
    OPEN_JOB_BASE_URL,
    OPEN_CODING_BASE_URL,       // Assuming a coding service might exist

    // Usernames for different services (consider more secure ways to handle credentials in production)
    OPEN_ACTOR_USERNAME,
    OPEN_CHART_USERNAME,
    OPEN_CHC_USERNAME,
    OPEN_DOCUMENT_USERNAME,
    OPEN_ERX_USERNAME,
    OPEN_LAB_USERNAME,
    OPEN_JOB_USERNAME,
    OPEN_CODING_USERNAME,

    // Passwords for different services (highly recommend using a secure vault for production)
    OPEN_ACTOR_PASSWORD,
    OPEN_CHART_PASSWORD,
    OPEN_CHC_PASSWORD,
    OPEN_DOCUMENT_PASSWORD,
    OPEN_ERX_PASSWORD,
    OPEN_LAB_PASSWORD,
    OPEN_JOB_PASSWORD,
    OPEN_CODING_PASSWORD,

    // Reporting Configuration
    OVERRIDEREPORTS,            // Whether to override existing reports (e.g., "yes" or "no")
    PASSEDSTEPSSCREENSHOT,      // Whether to take screenshots for passed steps
    FAILEDSTEPSSCREENSHOT,      // Whether to take screenshots for failed steps
    SKIPPEDSTEPSSCREENSHOT,     // Whether to take screenshots for skipped steps
    LOG_RESPONSE,               // Whether to log API responses in the report ("yes" or "no")

    // Database Configuration (for test data or logging results)
    DB_URL,                     // JDBC URL for the database
    DB_USERNAME,                // Database username
    DB_PASSWORD,                // Database password

    // Email Configuration (for sending test results)
    SEND_EMAIL,                 // Whether to send email notifications ("yes" or "no")
    EMAIL_HOST,                 // SMTP host for sending emails
    EMAIL_PORT,                 // SMTP port
    EMAIL_USERNAME,             // Email account username
    EMAIL_PASSWORD,             // Email account password
    EMAIL_FROM,                 // From email address
    EMAIL_TO_RECIPIENTS,        // Comma-separated list of To recipients
    EMAIL_CC_RECIPIENTS,        // Comma-separated list of CC recipients
    EMAIL_SUBJECT,              // Subject line for the email report

    // Other configurations
    EXPLICIT_WAIT_TIMEOUT,      // Default timeout for explicit waits (e.g., in seconds)
    RETRY_FAILED_TESTS;         // Whether to retry failed tests ("yes" or "no")

    // Note: Ensure all keys used in config.properties are listed here.
    // The actual values are fetched using PropertyUtils.getValue(ConfigProperties.KEY_NAME)
    // where KEY_NAME is one of the enum constants above.
    // The property names in the .properties file should match the lowercase version of these enum constants.
    // e.g., ENV enum corresponds to "env" key in config.properties.
}
