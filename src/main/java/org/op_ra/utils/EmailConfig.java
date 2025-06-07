package org.op_ra.utils;

import org.op_ra.enums.ConfigProperties; // Assuming ConfigProperties enum exists

/**
 * Configuration class for email settings.
 * This class retrieves email server details (host, port, credentials) and
 * email properties (sender, recipients, subject) from the framework'''s
 * configuration file via {@link PropertyUtils}.
 * <p>
 * The values obtained by this class are intended to be used by {@link SendEmailWithResults}
 * or any other email sending utility within the framework.
 * </p>
 * All methods are static and provide direct access to configured email properties.
 */
public final class EmailConfig {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private EmailConfig() {
        // Private constructor
    }

    /**
     * Checks if sending email notifications is enabled in the configuration.
     *
     * @return {@code true} if {@link ConfigProperties#SEND_EMAIL} is set to "yes" (case-insensitive),
     *         {@code false} otherwise.
     */
    public static boolean isEmailSendingEnabled() {
        try {
            return PropertyUtils.getValue(ConfigProperties.SEND_EMAIL).equalsIgnoreCase("yes");
        } catch (Exception e) {
            // If property is missing or there'''s an error, default to false
            System.err.println("EmailConfig: Could not read SEND_EMAIL property. Defaulting to false. Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the SMTP host for sending emails.
     *
     * @return The SMTP host string from {@link ConfigProperties#EMAIL_HOST}.
     *         Returns null if the property is not found (PropertyUtils would throw an exception).
     */
    public static String getEmailHost() {
        return PropertyUtils.getValue(ConfigProperties.EMAIL_HOST);
    }

    /**
     * Gets the SMTP port for sending emails.
     *
     * @return The SMTP port string from {@link ConfigProperties#EMAIL_PORT}.
     *         It'''s expected to be a number but returned as a string.
     */
    public static String getEmailPort() {
        return PropertyUtils.getValue(ConfigProperties.EMAIL_PORT);
    }

    /**
     * Gets the username for SMTP authentication.
     *
     * @return The email account username from {@link ConfigProperties#EMAIL_USERNAME}.
     */
    public static String getEmailUsername() {
        return PropertyUtils.getValue(ConfigProperties.EMAIL_USERNAME);
    }

    /**
     * Gets the password for SMTP authentication.
     *
     * @return The email account password from {@link ConfigProperties#EMAIL_PASSWORD}.
     *         <b>Security Note:</b> Storing passwords in properties files is not recommended for production.
     *         Consider using environment variables or a secure vault.
     */
    public static String getEmailPassword() {
        return PropertyUtils.getValue(ConfigProperties.EMAIL_PASSWORD);
    }

    /**
     * Gets the "From" email address for outgoing emails.
     *
     * @return The sender'''s email address from {@link ConfigProperties#EMAIL_FROM}.
     */
    public static String getFromEmailAddress() {
        return PropertyUtils.getValue(ConfigProperties.EMAIL_FROM);
    }

    /**
     * Gets the list of "To" recipients for email notifications.
     * The value is expected to be a comma-separated string of email addresses.
     *
     * @return Comma-separated string of "To" recipient email addresses from {@link ConfigProperties#EMAIL_TO_RECIPIENTS}.
     */
    public static String getToRecipients() {
        return PropertyUtils.getValue(ConfigProperties.EMAIL_TO_RECIPIENTS);
    }

    /**
     * Gets the list of "Cc" recipients for email notifications.
     * The value is expected to be a comma-separated string of email addresses.
     *
     * @return Comma-separated string of "Cc" recipient email addresses from {@link ConfigProperties#EMAIL_CC_RECIPIENTS}.
     *         May return an empty string or null if not configured (PropertyUtils behavior).
     */
    public static String getCcRecipients() {
        try {
            return PropertyUtils.getValue(ConfigProperties.EMAIL_CC_RECIPIENTS);
        } catch (Exception e) {
            // If CC recipients are optional and property might be missing
            System.out.println("EmailConfig: EMAIL_CC_RECIPIENTS property not found or error. Defaulting to no CC. Error: " + e.getMessage());
            return ""; // Return empty string if not found, assuming CC is optional
        }
    }

    /**
     * Gets the subject line for the email notifications.
     *
     * @return The email subject string from {@link ConfigProperties#EMAIL_SUBJECT}.
     */
    public static String getEmailSubject() {
        return PropertyUtils.getValue(ConfigProperties.EMAIL_SUBJECT);
    }
}
