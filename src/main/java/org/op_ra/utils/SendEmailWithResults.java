package org.op_ra.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail; // Using HtmlEmail for better formatting and attachments
import org.op_ra.constants.FrameworkConstants; // For report path
import java.io.File; // Import for File class

/**
 * Utility class for sending email notifications with test execution results.
 * This class uses Apache Commons Email library to construct and send emails.
 * Email server configuration and recipient details are fetched using {@link EmailConfig}.
 * <p>
 * The primary method {@link #sendEmail()} is typically called at the end of a test suite execution,
 * often from a TestNG listener (e.g., {@code ISuiteListener#onFinish}).
 * </p>
 * It attaches the main ExtentReport HTML file to the email.
 */
public final class SendEmailWithResults {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SendEmailWithResults() {
        // Private constructor
    }

    /**
     * Sends an email with the test execution report attached.
     * Email configuration (server, credentials, recipients, subject) is retrieved from {@link EmailConfig}.
     * The ExtentReport HTML file path is retrieved from {@link FrameworkConstants#getReportPath()}.
     * <p>
     * If email sending is disabled via {@link EmailConfig#isEmailSendingEnabled()}, this method does nothing.
     * </p>
     *
     * @throws RuntimeException if an error occurs during email construction or sending.
     *                          The underlying {@link org.apache.commons.mail.EmailException} is wrapped.
     */
    public static void sendEmail() {
        if (!EmailConfig.isEmailSendingEnabled()) {
            System.out.println("SendEmailWithResults: Email sending is disabled in configuration.");
            // FrameworkLogger.log(LogType.INFO, "Email sending is disabled.");
            return;
        }

        try {
            HtmlEmail email = new HtmlEmail(); // Use HtmlEmail for attachments and HTML content

            // Set SMTP server details
            email.setHostName(EmailConfig.getEmailHost());
            String portStr = EmailConfig.getEmailPort();
            if (portStr != null && !portStr.trim().isEmpty()) {
                 try {
                    email.setSmtpPort(Integer.parseInt(portStr));
                 } catch (NumberFormatException e) {
                     System.err.println("SendEmailWithResults: Invalid email port format: " + portStr + ". Using default port.");
                     // Log this error. Depending on the mail server, default might work or fail.
                 }
            }

            // Set authentication if username/password are provided
            String username = EmailConfig.getEmailUsername();
            String password = EmailConfig.getEmailPassword();
            if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
                email.setAuthenticator(new DefaultAuthenticator(username, password));
                email.setSSLOnConnect(true); // Common for modern SMTP; make this configurable if needed (e.g. setStartTLSEnabled)
            }

            // Set sender and recipients
            email.setFrom(EmailConfig.getFromEmailAddress());
            String toRecipients = EmailConfig.getToRecipients();
            if (toRecipients != null && !toRecipients.trim().isEmpty()) {
                for (String to : toRecipients.split(",")) {
                    email.addTo(to.trim());
                }
            } else {
                 System.err.println("SendEmailWithResults: No '''To''' recipients configured. Email will not be sent.");
                 return; // Or throw an error
            }

            String ccRecipients = EmailConfig.getCcRecipients();
            if (ccRecipients != null && !ccRecipients.trim().isEmpty()) {
                for (String cc : ccRecipients.split(",")) {
                    email.addCc(cc.trim());
                }
            }

            // Set email subject and body
            email.setSubject(EmailConfig.getEmailSubject() + " - " + DateUtils.getCurrentDateTime()); // Add timestamp to subject

            // Construct HTML body
            // You can make this more sophisticated with actual test summary data if available
            String htmlBody = "<html><body>"
                          + "<p>Hello Team,</p>"
                          + "<p>Please find attached the API test execution report for <b>"
                          + FrameworkConstants.getServiceName() // Assuming service name is available
                          + "</b> (Suite: " + FrameworkConstants.getReportClassName() + ")."
                          + "</p>"
                          + "<p>Execution completed on: " + DateUtils.getCurrentDateTime() + "</p>"
                          + "<p>Regards,<br>Automation Team</p>"
                          + "</body></html>";
            email.setHtmlMsg(htmlBody);
            // Set a plain text alternative for email clients that don'''t support HTML
            email.setTextMsg("Hello Team, Please find the attached API test execution report. Regards, Automation Team.");


            // Attach the ExtentReport
            String reportPath = FrameworkConstants.getReportPath();
            if (reportPath != null && !reportPath.isEmpty()) {
                File reportFile = new File(reportPath);
                if (reportFile.exists()) {
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setPath(reportPath);
                    attachment.setDisposition(EmailAttachment.ATTACHMENT);
                    attachment.setDescription("Test Execution Report");
                    attachment.setName(reportFile.getName()); // Set a user-friendly name for the attachment
                    email.attach(attachment);
                } else {
                    System.err.println("SendEmailWithResults: Report file not found at: " + reportPath + ". Email will be sent without attachment.");
                    // FrameworkLogger.log(LogType.WARN, "Report file not found for email attachment: " + reportPath);
                }
            } else {
                 System.err.println("SendEmailWithResults: Report path is empty. Email will be sent without attachment.");
            }

            // Send the email
            email.send();
            System.out.println("SendEmailWithResults: Email sent successfully!");
            // FrameworkLogger.log(LogType.INFO, "Test execution report email sent successfully.");

        } catch (org.apache.commons.mail.EmailException e) {
            System.err.println("SendEmailWithResults: Failed to send email. Error: " + e.getMessage());
            // FrameworkLogger.log(LogType.ERROR, "Failed to send email: " + e.getMessage());
            // e.printStackTrace(); // For debugging, consider logging stack trace
            throw new RuntimeException("Failed to send email with test results.", e);
        }
    }
}
