package org.op_ra.listeners;

import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.constants.FrameworkConstants;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.reports.ExtentReport;
import org.op_ra.utils.CurrentTestTracker;
import org.op_ra.utils.ExecutionContext;
import org.op_ra.utils.SendEmailWithResults;
import org.op_ra.utils.TestMetrics;
import org.op_ra.utils.TestRunContext;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.op_ra.enums.LogType.DEBUG;
import static org.op_ra.enums.LogType.ERROR;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;
import static org.op_ra.utils.ReportDatabaseController.logResult;

/**
 * {@code ListenerClass} is a TestNG listener that implements {@link ITestListener} and {@link ISuiteListener}.
 * This class is responsible for handling events during the test execution lifecycle, such as the start and
 * finish of a suite and the start, success, failure, and skipping of a test.
 *
 * <p>This listener plays a crucial role in generating Extent Reports, logging test results to a database,
 * and sending email notifications with the test summary.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To use this listener, it must be specified in the TestNG suite XML file.</p>
 *
 * <pre>{@code
 * <suite name="MyTestSuite">
 *     <listeners>
 *         <listener class-name="org.op_ra.listeners.ListenerClass"/>
 *     </listeners>
 *     <test name="MyTest">
 *         ...
 *     </test>
 * </suite>
 * }</pre>
 *
 * @see ITestListener
 * @see ISuiteListener
 * @see ExtentReport
 * @version 1.0
 * @since 2023-10-27
 */
public class ListenerClass implements ITestListener, ISuiteListener {

    private static long suiteStartTime;
    private static int passedCount = 0;
    private static int failedCount = 0;
    private static int skippedCount = 0;

    /**
     * This method is invoked before the TestNG suite starts. It initializes the Extent Report,
     * sets up the execution context with a unique run ID, and resets the test counters.
     *
     * @param suite The suite that is about to be run.
     */
    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = System.currentTimeMillis();
        passedCount = 0;
        failedCount = 0;
        skippedCount = 0;
        ExtentReport.initReports(suite.getXmlSuite().getName());
        ExecutionContext.initRunIdIfAbsent();
        ExecutionContext.setBuildVersion(System.getProperty("buildVersion", ExecutionContext.getBuildVersion()));
        ExecutionContext.setTriggeredBy(System.getProperty("triggeredBy", ExecutionContext.getTriggeredBy()));
    }

    /**
     * This method is invoked after the TestNG suite has run. It flushes the Extent Report,
     * calculates the total execution time, and sends an email with the test results summary.
     *
     * @param suite The suite that has just been run.
     */
    @Override
    public void onFinish(ISuite suite) {
        try {
            ExtentReport.flushReports();

            long suiteEndTime = System.currentTimeMillis();
            long durationMillis = suiteEndTime - suiteStartTime;
            String executionDuration = formatDuration(durationMillis);

            SendEmailWithResults.sendEmail(passedCount, failedCount, skippedCount, executionDuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is invoked before each test method starts. It creates a new test in the Extent Report,
     * adds the author and category information from the {@link FrameworkAnnotation}, and resets the
     * test run context if the test method is different from the previous one.
     *
     * @param result The result of the test method that is about to be run.
     */
    @Override
    public void onTestStart(ITestResult result) {
        FrameworkAnnotation annotation = result.getMethod().getConstructorOrMethod()
                .getMethod().getAnnotation(FrameworkAnnotation.class);
        ExtentReport.createTest("<span style='color: green;'>TestCase Name:</span>"
                + result.getMethod().getMethodName());
        ExtentReport.addAuthors(annotation.author());
        ExtentReport.addCategories(annotation.category());
        String methodName = result.getMethod().getMethodName();
        if (!methodName.equals(CurrentTestTracker.get())) {
            TestRunContext.reset();
            CurrentTestTracker.set(methodName);
        }
        TestMetrics.clear();
    }

    /**
     * This method is invoked when a test method passes. It logs the test result to the database,
     * marks the test as "pass" in the Extent Report, and clears the test context.
     *
     * @param result The result of the successful test method.
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
     * This method is invoked when a test method fails. It logs the test result to the database,
     * marks the test as "fail" in the Extent Report, logs the exception, and clears the test metrics.
     *
     * @param result The result of the failed test method.
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
        TestMetrics.clear();
    }

    /**
     * This method is invoked when a test method is skipped. It logs the test result to the database,
     * marks the test as "skip" in the Extent Report, and clears the test context.
     *
     * @param result The result of the skipped test method.
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
     * Formats a duration in milliseconds to a string in HH:MM:SS format.
     *
     * @param durationMillis The duration in milliseconds.
     * @return A string representing the duration in HH:MM:SS format.
     */
    private String formatDuration(long durationMillis) {
        long seconds = durationMillis / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
