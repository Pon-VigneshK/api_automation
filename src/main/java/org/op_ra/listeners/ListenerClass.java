package org.op_ra.listeners;

import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.reports.ExtentReport;
import org.op_ra.utils.SendEmailWithResults;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

import static org.op_ra.enums.LogType.*;
import static org.op_ra.reports.FrameworkLogger.log;
import static org.op_ra.utils.ReportDatabaseController.storeReportInDatabase;

/**
 * Implements {@link org.testng.ITestListener} and {@link org.testng.ISuiteListener} to leverage the abstract methods.
 * Mostly used to assist in extent report generation.
 * <pre>Please make sure to add the listener details in the testng.xml file.</pre>
 */
public class ListenerClass implements ITestListener, ISuiteListener {

    /**
     * Initializes the reports with the specified file name.
     *
     * @see ExtentReport
     */
    @Override
    public void onStart(ISuite suite) {
        ExtentReport.initReports(suite.getXmlSuite().getName());
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
            SendEmailWithResults.sendEmail();
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
//        ExtentReport.addAuthors(annotation.author());
//        ExtentReport.addCategories(annotation.category());
    }

    /**
     * Marks the test as pass and logs it in the report.
     *
     * @see ExtentLogger
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        storeReportInDatabase(result.getMethod().getMethodName(), "Pass");
        ExtentLogger.pass(result.getMethod().getMethodName() + " is passed !!");
        log(INFO, result.getMethod().getMethodName() + " is passed !!");
        log(INFO, "------------------------------------------------------------");
    }

    /**
     * Marks the test as fail and logs it in the report.
     *
     * @see org.op_ra.reports.ExtentLogger
     */
    @Override
    public void onTestFailure(ITestResult result) {
        storeReportInDatabase(result.getMethod().getMethodName(), "Fail");
        ExtentLogger.fail(result.getThrowable().getMessage());
        ExtentLogger.fail(result.getThrowable().toString());
        log(DEBUG, Arrays.toString(result.getThrowable().getStackTrace()));
        log(ERROR, result.getMethod().getMethodName() + " is failed !!");
        log(INFO, "------------------------------------------------------------");
    }

    /**
     * Marks the test as skip and logs it in the report.
     *
     * @see org.op_ra.reports.ExtentLogger
     */

    @Override
    public void onTestSkipped(ITestResult result) {
        storeReportInDatabase(result.getMethod().getMethodName(), "Skip");
        ExtentLogger.skip(result.getMethod().getMethodName() + " is skipped !!");
        log(INFO, result.getMethod().getMethodName() + " is skipped !!");
        log(INFO, "------------------------------------------------------------");
    }
}
