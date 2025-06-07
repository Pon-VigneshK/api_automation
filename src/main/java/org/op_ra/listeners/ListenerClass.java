package org.op_ra.listeners;

import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.reports.ExtentReport;
import org.op_ra.reports.FrameworkLogger;
import org.op_ra.utils.SendEmailWithResults;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException; // Added import for IOException
import java.util.Arrays;

import static org.op_ra.enums.LogType.*;
import static org.op_ra.reports.FrameworkLogger.log;
import static org.op_ra.utils.ReportDatabaseController.storeReportInDatabase;

/**
 * Implements {@link org.testng.ITestListener} and {@link org.testng.ISuiteListener}
 * to customize TestNG test execution lifecycle.
 * This class is primarily used for initializing and flushing ExtentReports,
 * creating test entries in the report, logging test status (pass/fail/skip),
 * and triggering actions like sending email notifications or storing results in a database.
 * <br>
 * Ensure this listener is configured in your TestNG XML suite file:
 * <pre>{@code
 * <listeners>
 *     <listener class-name="org.op_ra.listeners.ListenerClass"/>
 * </listeners>
 * }</pre>
 */
public class ListenerClass implements ITestListener, ISuiteListener {

    /**
     * Initializes the ExtentReports at the beginning of a TestNG suite execution.
     * Retrieves the suite name from the TestNG XML file to be used in the report.
     *
     * @param suite The TestNG suite that is about to start.
     * @see ExtentReport#initReports(String)
     */
    @Override
    public void onStart(ISuite suite) {
        ExtentReport.initReports(suite.getXmlSuite().getName());
        log(INFO, "Test Suite Started: " + suite.getName());
    }

    /**
     * Flushes the ExtentReports at the end of a TestNG suite execution.
     * This ensures all test information is written to the report file.
     * Also triggers sending an email with the test results.
     *
     * @param suite The TestNG suite that has finished.
     * @see ExtentReport#flushReports()
     * @see SendEmailWithResults#sendEmail()
     */
    @Override
    public void onFinish(ISuite suite) {
        try {
            ExtentReport.flushReports();
            SendEmailWithResults.sendEmail(); // Consider making this configurable
            log(INFO, "Test Suite Finished: " + suite.getName());
        } catch (IOException e) { // Catch specific IOException
            log(ERROR, "Error during report flushing or email sending: " + e.getMessage());
            // Consider re-throwing as a runtime exception if it is critical
            // throw new RuntimeException("Error finalizing reports or sending email", e);
        } catch (Exception e) { // Catch other potential exceptions
            log(ERROR, "An unexpected error occurred during suite finish: " + e.getMessage());
            // throw new RuntimeException("Unexpected error during suite finish", e);
        }
    }

    /**
     * Creates a new test entry in the ExtentReport when a TestNG test method starts.
     * Retrieves author and category information from the {@link FrameworkAnnotation}
     * if present on the test method.
     *
     * @param result The result of the test method that is about to start.
     * @see ExtentReport#createTest(String)
     * @see ExtentReport#addAuthors(String[])
     * @see ExtentReport#addCategories(org.op_ra.enums.CategoryType[])
     * @see FrameworkAnnotation
     */
    @Override
    public void onTestStart(ITestResult result) {
        FrameworkAnnotation annotation = result.getMethod().getConstructorOrMethod()
                .getMethod().getAnnotation(FrameworkAnnotation.class);
        ExtentReport.createTest("<span style='font-weight:bold;color:darkblue;'>Test Case: </span>"
                + result.getMethod().getMethodName()); // Enhanced test name display
        if (annotation != null) {
            ExtentReport.addAuthors(annotation.author());
            ExtentReport.addCategories(annotation.category());
        }
        log(INFO, "Test Started: " + result.getMethod().getMethodName());
    }

    /**
     * Logs the test as "PASS" in the ExtentReport and stores the result in the database.
     *
     * @param result The result of the successfully completed test method.
     * @see ExtentLogger#pass(String)
     * @see org.op_ra.utils.ReportDatabaseController#storeReportInDatabase(String, String)
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        storeReportInDatabase(result.getMethod().getMethodName(), "Pass");
        ExtentLogger.pass("<span style='font-weight:bold;color:green;'>PASSED: </span>" + result.getMethod().getMethodName());
        log(INFO, result.getMethod().getMethodName() + " is PASSED.");
        log(INFO, "------------------------------------------------------------");
    }

    /**
     * Logs the test as "FAIL" in the ExtentReport, including the exception message and stack trace.
     * Stores the result in the database.
     *
     * @param result The result of the failed test method.
     * @see ExtentLogger#fail(String)
     * @see org.op_ra.utils.ReportDatabaseController#storeReportInDatabase(String, String)
     */
    @Override
    public void onTestFailure(ITestResult result) {
        storeReportInDatabase(result.getMethod().getMethodName(), "Fail");
        String testName = result.getMethod().getMethodName();
        ExtentLogger.fail("<span style='font-weight:bold;color:red;'>FAILED: </span>" + testName);
        ExtentLogger.fail("Failure Reason: " + result.getThrowable().getMessage());
        // ExtentLogger.fail("<details><summary>Click to see Stack Trace</summary>"
        //         + Arrays.toString(result.getThrowable().getStackTrace()).replaceAll(",", "<br>")
        //         + "</details>"); // Logging stack trace in a collapsible way
        log(ERROR, testName + " is FAILED due to: " + result.getThrowable().getMessage());
        FrameworkLogger.log(DEBUG, Arrays.toString(result.getThrowable().getStackTrace())); // Use FrameworkLogger for consistency
        log(INFO, "------------------------------------------------------------");
    }

    /**
     * Logs the test as "SKIP" in the ExtentReport and stores the result in the database.
     *
     * @param result The result of the skipped test method.
     * @see ExtentLogger#skip(String)
     * @see org.op_ra.utils.ReportDatabaseController#storeReportInDatabase(String, String)
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        storeReportInDatabase(result.getMethod().getMethodName(), "Skip");
        ExtentLogger.skip("<span style='font-weight:bold;color:orange;'>SKIPPED: </span>" + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            ExtentLogger.skip("Skip Reason: " + result.getThrowable().getMessage());
            log(INFO, result.getMethod().getMethodName() + " is SKIPPED due to: " + result.getThrowable().getMessage());
        } else {
            log(INFO, result.getMethod().getMethodName() + " is SKIPPED.");
        }
        log(INFO, "------------------------------------------------------------");
    }
}
