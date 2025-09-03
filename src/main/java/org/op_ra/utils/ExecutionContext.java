package org.op_ra.utils;

import org.op_ra.constants.FrameworkConstants;

import java.util.UUID;

public final class ExecutionContext {

    private static final String pipelineId = System.getenv("CI_PIPELINE_ID");

    private static volatile String runId;
    private static String serviceName = FrameworkConstants.getServiceName();
    private static volatile String buildVersion = (pipelineId != null && !pipelineId.isEmpty()) ? pipelineId : "";
    private static volatile String triggeredBy = (pipelineId != null && !pipelineId.isEmpty()) ? "gitlab-ci" : "manual-run";

    private ExecutionContext() {
    }

    /** Generate a unique runId once per suite. Safe to call multiple times; first wins. */
    public static synchronized void initRunIdIfAbsent() {
        if (runId == null || runId.isEmpty()) {
            runId = UUID.randomUUID().toString();
        }
    }

    public static String getRunId() { return runId; }
    public static void setRunId(String value) { runId = value; }

    public static String getServiceName() { return serviceName; }
    public static void setServiceName(String value) { serviceName = value; }

    public static String getBuildVersion() { return buildVersion; }
    public static void setBuildVersion(String value) { buildVersion = value; }

    public static String getTriggeredBy() { return triggeredBy; }
    public static void setTriggeredBy(String value) { triggeredBy = value; }
}
