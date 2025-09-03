package org.op_ra.listeners;

import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.constants.FrameworkConstants;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.reports.ExtentReport;
import org.op_ra.utils.*;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.op_ra.enums.LogType.*;
import static org.op_ra.reports.FrameworkLogger.log;
import static org.op_ra.utils.ReportDatabaseController.logResult;

/**
 * Implements {@link org.testng.ITestListener} and {@link org.testng.ISuiteListener} to leverage the abstract methods.
 * Mostly used to assist in extent report generation.
 * <pre>Please make sure to add the listener details in the testng.xml file.</pre>
 */
public class ListenerClass implements ITestListener, ISuiteListener {

    private static long suiteStartTime;
    private static int passedCount = 0;
    private static int failedCount = 0;
    private static int skippedCount = 0;

    /**
     * Initializes the reports with the specified file name.
     *
     * @see ExtentReport
     */
    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = System.currentTimeMillis();
        passedCount = 0;
        failedCount = 0;
        skippedCount = 0;
        // Initialize Extent and generate a unique runId for this suite
        ExtentReport.initReports(suite.getXmlSuite().getName());
        ExecutionContext.initRunIdIfAbsent();
        // Optional: pick buildVersion/triggeredBy from system properties if provided
        ExecutionContext.setBuildVersion(System.getProperty("buildVersion", ExecutionContext.getBuildVersion()));
        ExecutionContext.setTriggeredBy(System.getProperty("triggeredBy", ExecutionContext.getTriggeredBy()));
    }

    /**
     * Terminates the reports.
     *
     * @see ExtentReport
     */
    @Override
    public void onFinish(ISuite suite) {
        try {
            ExtentReport.flushReports();

            // Calculate execution duration
            long suiteEndTime = System.currentTimeMillis();
            long durationMillis = suiteEndTime - suiteStartTime;
            String executionDuration = formatDuration(durationMillis);

            // Send email with actual test counts and duration
            SendEmailWithResults.sendEmail(passedCount, failedCount, skippedCount, executionDuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts a test node for each TestNG test.
     *
     * @see ExtentReport
     * @see FrameworkAnnotation
     */
    @Override
    public void onTestStart(ITestResult result) {
        FrameworkAnnotation annotation = result.getMethod().getConstructorOrMethod()
                .getMethod().getAnnotation(FrameworkAnnotation.class);
        ExtentReport.createTest("<span style='color: green;'>TestCase Name:</span>"
                + result.getMethod().getMethodName());
        ExtentReport.addAuthors(annotation.author());
        ExtentReport.addCategories(annotation.category());
        // Reset iteration when test method changes; otherwise keep for retries
        String methodName = result.getMethod().getMethodName();
        if (!methodName.equals(CurrentTestTracker.get())) {
            TestRunContext.reset();
            CurrentTestTracker.set(methodName);
        }
        // Clear per-test metrics at start
        TestMetrics.clear();
    }

    /**
     * Marks the test as pass and logs it in the report.
     *
     * @see ExtentLogger
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        passedCount++;
        logResult(
                ExecutionContext.getRunId(),
                TestRunContext.getIterationId(),
                ExecutionContext.getServiceName(),
                result.getMethod().getMethodName(),
                "Pass",
                FrameworkConstants.getBranchName(),
                TestMetrics.getResponseTimeMs(),
                TestMetrics.getHttpStatusCode(),
                ExecutionContext.getBuildVersion(),
                ExecutionContext.getTriggeredBy(),
                null,
                LocalDateTime.now()
        );
        ExtentLogger.pass(result.getMethod().getMethodName() + " is passed !!");
        log(INFO, result.getMethod().getMethodName() + " is passed !!");
        log(INFO, "------------------------------------------------------------");
        TestRunContext.clear();
        TestMetrics.clear();
    }

    /**
     * Marks the test as fail and logs it in the report.
     *
     * @see org.op_ra.reports.ExtentLogger
     */
    @Override
    public void onTestFailure(ITestResult result) {
        failedCount++;
        String err = result.getThrowable() != null ? result.getThrowable().toString() : null;
        logResult(
                ExecutionContext.getRunId(),
                TestRunContext.getIterationId(),
                ExecutionContext.getServiceName(),
                result.getMethod().getMethodName(),
                "Fail",
                FrameworkConstants.getBranchName(),
                TestMetrics.getResponseTimeMs(),
                TestMetrics.getHttpStatusCode(),
                ExecutionContext.getBuildVersion(),
                ExecutionContext.getTriggeredBy(),
                err,
                LocalDateTime.now()
        );
        ExtentLogger.fail(result.getThrowable() != null ? result.getThrowable().getMessage() : "Test failed");
        if (result.getThrowable() != null) {
            ExtentLogger.fail(result.getThrowable().toString());
        }
        log(DEBUG, Arrays.toString(result.getThrowable() != null ? result.getThrowable().getStackTrace() : new StackTraceElement[]{}));
        log(ERROR, result.getMethod().getMethodName() + " is failed !!");
        log(INFO, "------------------------------------------------------------");
        // Do not clear TestRunContext here; a retry may follow and needs the iterationId preserved
        TestMetrics.clear();
    }

    /**
     * Marks the test as skip and logs it in the report.
     *
     * @see org.op_ra.reports.ExtentLogger
     */

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedCount++;
        logResult(
                ExecutionContext.getRunId(),
                TestRunContext.getIterationId(),
                ExecutionContext.getServiceName(),
                result.getMethod().getMethodName(),
                "Skip",
                FrameworkConstants.getBranchName(),
                TestMetrics.getResponseTimeMs(),
                TestMetrics.getHttpStatusCode(),
                ExecutionContext.getBuildVersion(),
                ExecutionContext.getTriggeredBy(),
                null,
                LocalDateTime.now()
        );
        ExtentLogger.skip(result.getMethod().getMethodName() + " is skipped !!");
        log(INFO, result.getMethod().getMethodName() + " is skipped !!");
        log(INFO, "------------------------------------------------------------");
        TestRunContext.clear();
        TestMetrics.clear();
    }

    /**
     * Format duration from milliseconds to HH:MM:SS format
     */
    private String formatDuration(long durationMillis) {
        long seconds = durationMillis / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
