package org.op_ra.requestbuilder;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.utils.PropertyUtils;

import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;

public class ApiActions {

    public static String buildActorServiceEndpoint(String endpoint, String... params) {
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.OPEN_ACTOR_BASE_URL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting GET endpoint - " + fullEndpoint);
        ExtentLogger.log("Requesting GET endpoint - " + fullEndpoint);
        return fullEndpoint;
    }
    public static String buildChartServiceEndpoint(String endpoint, String... params) {
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.OPEN_CHART_BASE_URL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting GET endpoint - " + fullEndpoint);
        ExtentLogger.log("Requesting GET endpoint - " + fullEndpoint);
        return fullEndpoint;
    }
    public static String buildChcServiceEndpoint(String endpoint, String... params) {
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.OPEN_CHC_BASE_URL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting GET endpoint - " + fullEndpoint);
        ExtentLogger.log("Requesting GET endpoint - " + fullEndpoint);
        return fullEndpoint;
    }
    public static String buildCodingServiceEndpoint(String endpoint, String... params) {
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.OPEN_CODING_BASE_URL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting GET endpoint - " + fullEndpoint);
        ExtentLogger.log("Requesting GET endpoint - " + fullEndpoint);
        return fullEndpoint;
    }
    public static String buildDocumentServiceEndpoint(String endpoint, String... params) {
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.OPEN_DOCUMENT_BASE_URL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting GET endpoint - " + fullEndpoint);
        ExtentLogger.log("Requesting GET endpoint - " + fullEndpoint);
        return fullEndpoint;
    }
    public static String buildErxServiceEndpoint(String endpoint, String... params) {
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.OPEN_ERX_BASE_URL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting GET endpoint - " + fullEndpoint);
        ExtentLogger.log("Requesting GET endpoint - " + fullEndpoint);
        return fullEndpoint;
    }public static String buildLabServiceEndpoint(String endpoint, String... params) {
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.OPEN_LAB_BASE_URL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting GET endpoint - " + fullEndpoint);
        ExtentLogger.log("Requesting GET endpoint - " + fullEndpoint);
        return fullEndpoint;
    }
    public static String buildJobServiceEndpoint(String endpoint, String... params) {
        String fullEndpoint = PropertyUtils.getValue(ConfigProperties.OPEN_JOBS_BASE_URL) + String.format(endpoint, (Object[]) params);
        log(INFO, "Requesting GET endpoint - " + fullEndpoint);
        ExtentLogger.log("Requesting GET endpoint - " + fullEndpoint);
        return fullEndpoint;
    }

}
