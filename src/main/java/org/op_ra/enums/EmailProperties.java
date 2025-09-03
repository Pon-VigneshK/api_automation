package org.op_ra.enums;

/**
 * {@code EmailProperties} is an enumeration that defines the keys for email configuration.
 * This enum is used to retrieve email settings from a configuration source, such as a properties file.
 * Using this enum ensures consistency and avoids errors from typos when accessing email properties.
 *
 * <p><b>Usage:</b></p>
 * <p>This enum is used with a utility class to get email configuration values.</p>
 *
 * <pre>{@code
 * String fromEmail = EmailPropertyUtils.get(EmailProperties.FROMEMAIL);
 * String emailPassword = EmailPropertyUtils.get(EmailProperties.EMAILPASSWORD);
 * }</pre>
 *
 * <p>The available properties are:</p>
 * <ul>
 *   <li>{@link #FROMEMAIL} - The email address from which the test results will be sent.</li>
 *   <li>{@link #EMAILPASSWORD} - The password for the email account.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public enum EmailProperties {

    /**
     * The sender's email address, used in the "From" field of the email.
     */
    FROMEMAIL,

    /**
     * The password for the sender's email account, used for authentication.
     */
    EMAILPASSWORD;

}
