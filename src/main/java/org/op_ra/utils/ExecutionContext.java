package org.op_ra.utils;

import org.op_ra.constants.FrameworkConstants;

import java.util.UUID;

/**
 * {@code ExecutionContext} is a final class that holds information about the current test execution context.
 * This class provides a centralized place to store and access details such as the run ID, service name,
 * build version, and who triggered the execution.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to get and set the execution context details.</p>
 *
 * <pre>{@code
 * // Initializing the run ID
 * ExecutionContext.initRunIdIfAbsent();
 *
 * // Getting the run ID
 * String runId = ExecutionContext.getRunId();
 *
 * // Setting the build version
 * ExecutionContext.setBuildVersion("1.2.3");
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public final class ExecutionContext {

    private static final String pipelineId = System.getenv("CI_PIPELINE_ID");

    private static volatile String runId;
    private static String serviceName = FrameworkConstants.getServiceName();
    private static volatile String buildVersion = (pipelineId != null && !pipelineId.isEmpty()) ? pipelineId : "";
    private static volatile String triggeredBy = (pipelineId != null && !pipelineId.isEmpty()) ? "gitlab-ci" : "manual-run";

    private ExecutionContext() {
    }

    /**
     * Initializes the run ID with a unique UUID if it has not already been initialized.
     * This method is thread-safe.
     */
    public static synchronized void initRunIdIfAbsent() {
        if (runId == null || runId.isEmpty()) {
            runId = UUID.randomUUID().toString();
        }
    }

    /**
     * Returns the current run ID.
     *
     * @return The current run ID.
     */
    public static String getRunId() {
        return runId;
    }

    /**
     * Sets the run ID.
     *
     * @param value The run ID to set.
     */
    public static void setRunId(String value) {
        runId = value;
    }

    /**
     * Returns the name of the service under test.
     *
     * @return The service name.
     */
    public static String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the name of the service under test.
     *
     * @param value The service name to set.
     */
    public static void setServiceName(String value) {
        serviceName = value;
    }

    /**
     * Returns the current build version.
     *
     * @return The current build version.
     */
    public static String getBuildVersion() {
        return buildVersion;
    }

    /**
     * Sets the build version.
     *
     * @param value The build version to set.
     */
    public static void setBuildVersion(String value) {
        buildVersion = value;
    }

    /**
     * Returns who triggered the current execution.
     *
     * @return Who triggered the execution.
     */
    public static String getTriggeredBy() {
        return triggeredBy;
    }

    /**
     * Sets who triggered the execution.
     *
     * @param value Who triggered the execution.
     */
    public static void setTriggeredBy(String value) {
        triggeredBy = value;
    }
}
