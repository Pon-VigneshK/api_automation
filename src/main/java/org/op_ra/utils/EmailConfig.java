package org.op_ra.utils;

import org.op_ra.constants.FrameworkConstants;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EmailConfig {

    private final Properties properties;

    public EmailConfig() {
        String configFilePath = FrameworkConstants.getMailConfig();
        properties = new Properties();
        try (FileReader fileReader = new FileReader(configFilePath)) {
            properties.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from configuration file", e);
        }
    }

    public String getSendExecutionResultsInEmail() {
        return properties.getProperty("sendexecutionresultsinemail");
    }

    public String getFromEmail() {
        return properties.getProperty("fromemail");
    }

    public String getEmailPassword() {
        return properties.getProperty("emailpassword");
    }

    public String getToEmails() {
        return properties.getProperty("toemails");
    }

    public String getCcEmails() {
        return properties.getProperty("ccemails");
    }

    public String getBccEmails() {
        return properties.getProperty("bccemails");
    }
}
