package org.op_ra.utils;

import org.op_ra.constants.FrameworkConstants;

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
import java.util.Properties;


public class SendEmailWithResults {

    public static void sendEmail() {
        EmailConfig emailConfig = new EmailConfig();

        if (emailConfig.getSendExecutionResultsInEmail().equalsIgnoreCase("yes")) {
            String attachmentPath = FrameworkConstants.getReportPath();
            String greeting;
            LocalTime currentTime = LocalTime.now();
            LocalDate currentDate = LocalDate.now();
            LocalTime morningEnd = LocalTime.of(12, 0);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(dateFormatter);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String formattedTime = currentTime.format(timeFormatter);

            if (currentTime.isBefore(morningEnd)) {
                greeting = "Good Morning";
            } else {
                greeting = "Good Afternoon";
            }

            String subject = "OPEN Service Automation Report for " + formattedDate + " Execution";
            String body = "Hi Team,\n\n" +
                    greeting + ",\n\n" +
                    "Please find the OPEN service automation execution report for " + formattedTime + ". Kindly review it.\n\n" +
                    "Thank you,";
            String from = emailConfig.getFromEmail();
            String host = "smtp.gmail.com";
            String password = emailConfig.getEmailPassword();
            String to =  emailConfig.getToEmails();
            String cc = emailConfig.getCcEmails();
            String bcc = emailConfig.getBccEmails();

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

                if (cc != null && !cc.isEmpty()) {
                    String[] ccEmails = cc.split(",");
                    for (String ccEmail : ccEmails) {
                        message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccEmail.trim()));
                    }
                }
                if (bcc != null && !bcc.isEmpty()) {
                    String[] bccEmails = bcc.split(",");
                    for (String bccEmail : bccEmails) {
                        message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bccEmail.trim()));
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
                messageBodyPart.setFileName("OPEN_Service_" + formattedDate + "_" + formattedTime + ".html");
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);

                Transport.send(message);
                System.out.println("Email Sent Successfully.");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }

    }


}