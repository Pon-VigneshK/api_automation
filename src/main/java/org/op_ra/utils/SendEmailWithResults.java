package org.op_ra.utils;

import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.ConfigProperties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.op_ra.constants.FrameworkConstants.getBranchName;
import static org.op_ra.enums.EmailProperties.EMAILPASSWORD;
import static org.op_ra.enums.EmailProperties.FROMEMAIL;
import static org.op_ra.utils.EmailPropertyUtils.getValue;


public class SendEmailWithResults {

    public static void sendEmail(int passedCount, int failedCount, int skippedCount, String executionDuration) {
        Map<String, String> data = new HashMap<>();
        String body;
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        LocalTime morningEnd = LocalTime.of(12, 0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(dateFormatter);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = currentTime.format(timeFormatter);
        String greeting;
        if (currentTime.isBefore(morningEnd)) {
            greeting = "Good Morning";
        } else {
            greeting = "Good Afternoon";
        }


        String subject = "";
        String environment = "";
        switch (getBranchName()) {
            case "main":
                environment = "main";
                subject = "Atlas Practice Service - UAT/Staging Test Automation Report for the Execution on " + formattedDate + " at " + formattedTime;
                break;
            case "dev-qe":
                environment = "dev-qe";
                subject = "Atlas Practice Service - QE Unit Test Automation Report for the Execution on " + formattedDate + " at " + formattedTime;
                break;
            case "dry-run":
                environment = "dry-run";
                subject = "Atlas Practice Service - Dry Run Automation Test Report for the Execution on " + formattedDate + " at " + formattedTime;
                break;
            default:
                environment = "local";
                subject = "Atlas Practice Service - Local Run Automation Test Execution Report ( " + formattedDate + " , " + formattedTime + " )";
                break;
        }

        String runmode = PropertyUtils.getValue(ConfigProperties.RUNMODE);
        if (runmode.equalsIgnoreCase("remote")) {

            String query = String.format("SELECT * FROM service_automation_runner_manager.atlas_practice_automation_alert_recipients WHERE git_branch = '%s';", environment);
            data = DataBaseConnectionUtils.retrieveRowData(query);
        } else if (runmode.equalsIgnoreCase("local")) {
            String query = String.format("SELECT * FROM atlas_practice_automation_alert_recipients WHERE git_branch = '%s';", environment);
            data = DataBaseConnectionUtils.retrieveRowDataSqLite(query);
        }

        if (data.get("email_enabled").equalsIgnoreCase("yes") && environment != null) {
            String attachmentPath = FrameworkConstants.getReportPath();

            // Get CI environment variables
            String branchName = System.getenv("CI_COMMIT_REF_NAME");
            String repoName = System.getenv("CI_PROJECT_NAME");
            String pipelineId = System.getenv("CI_PIPELINE_ID");
            String pipelineUrl = System.getenv("CI_PIPELINE_URL");

            // Calculate total tests
            int totalTests = passedCount + failedCount + skippedCount;

            // Determine execution environment based on branch
            String executionEnvironment = "";
            switch (environment) {
                case "main":
                    executionEnvironment = "UAT/Staging";
                    break;
                case "dev-qe":
                    executionEnvironment = "QE";
                    break;
                case "dry-run":
                    executionEnvironment = "Dry Run";
                    break;
                default:
                    executionEnvironment = "Local";
                    break;
            }
            if (!executionEnvironment.equals("Local")) {
                body = "Hi Team,\n\n" +
                        "Please find attached the Atlas Practice Service Automation Execution Report for the latest test cycle. Below are the execution details:\n\n" +
                        "Execution Summary:\n" +
                        "==================\n" +
                        "Project / Module: " + repoName + "\n" +
                        "Execution Environment: " + executionEnvironment + "\n" +
                        "Triggered From: GitLab CI/CD Pipeline\n" +
                        "Branch: " + (branchName != null ? branchName : getBranchName()) + "\n" +
                        "Execution Date & Time: " + formattedDate + " at " + formattedTime + " IST\n" +
                        "Total Test Cases Executed: " + totalTests + "\n" +
                        "Passed: " + passedCount + "\n" +
                        "Failed: " + failedCount + "\n" +
                        "Skipped: " + skippedCount + "\n" +
                        "Execution Duration: " + executionDuration + "\n" +
                        "Pipeline ID: " + (pipelineId != null ? pipelineId : "N/A") + "\n" +
                        "Pipeline URL: " + (pipelineUrl != null ? pipelineUrl : "N/A") + "\n\n" +
                        "======================\n" +
                        "Attachment:\n" +
                        "======================\n" +
                        "AtlasPractice_Service_" + formattedDate + "_" + formattedTime + ".html – Detailed Automation Execution Report\n\n" +
                        "Kindly review the report and share any feedback or required follow-ups.\n\n" +
                        "Regards,\n" +
                        "QE Automation Team";
            } else {
                body = "Hi Team,\n\n" +
                        "Please find attached the Atlas Practice Service Automation Execution Report for the latest test cycle. Below are the execution details:\n\n" +
                        "Execution Summary:\n" +
                        "==================\n" +
                        "Project / Module: " + FrameworkConstants.getServiceName() + "\n" +
                        "Execution Environment: " + executionEnvironment + "\n" +
                        "Triggered From: Local\n" +
                        "Execution Date & Time: " + formattedDate + " at " + formattedTime + " IST\n" +
                        "Total Test Cases Executed: " + totalTests + "\n" +
                        "Passed: " + passedCount + "\n" +
                        "Failed: " + failedCount + "\n" +
                        "Skipped: " + skippedCount + "\n" +
                        "Execution Duration: " + executionDuration + "\n" +
                        "======================\n" +
                        "Attachment:\n" +
                        "======================\n" +
                        "AtlasPractice_Service_" + formattedDate + "_" + formattedTime + ".html – Detailed Automation Execution Report\n\n" +
                        "Kindly review the report and share any feedback or required follow-ups.\n\n" +
                        "Regards,\n" +
                        "QE Automation Team";
            }

            String from = getValue(FROMEMAIL);
            String host = "smtp.gmail.com";
            String password = getValue(EMAILPASSWORD);


            String to = data.get("email_address");

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));

                if (to != null && !to.isEmpty()) {
                    String[] toEmails = to.split(",");
                    for (String toEmail : toEmails) {
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail.trim()));
                    }
                }
                message.setSubject(subject);

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(body);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                // Attach the report file
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentPath);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName("AtlasPractice_Service_" + formattedDate + "_" + formattedTime + ".html");
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);

                Transport.send(message);
                System.out.println("Email Sent Successfully.");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }

    }

    /**
     * Backward compatibility method - calls the main sendEmail method with default counts
     */
    public static void sendEmail() {
        sendEmail(0, 0, 0, "00:00:00");
    }

    /**
     * Backward compatibility method - calls the main sendEmail method with default duration
     */
    public static void sendEmail(int passedCount, int failedCount, int skippedCount) {
        sendEmail(passedCount, failedCount, skippedCount, "00:00:00");
    }


}