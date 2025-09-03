package org.op_ra.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.CategoryType;

import java.io.IOException;
import java.util.Objects;

import static org.op_ra.constants.FrameworkConstants.getReportPath;
import static org.op_ra.constants.FrameworkConstants.getServiceName;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;

/**
 * {@code ExtentReport} is a final class that provides methods for initializing and managing
 * the ExtentReports instance. This class is responsible for setting up the report configuration,
 * creating test entries, and flushing the report at the end of the test suite.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class are called by the {@link org.op_ra.listeners.ListenerClass}
 * to manage the lifecycle of the Extent Report.</p>
 *
 * <pre>{@code
 * // Initializing the report at the start of the suite
 * ExtentReport.initReports("MyTestSuite");
 *
 * // Creating a test entry at the start of a test
 * ExtentReport.createTest("MyTestCase");
 *
 * // Flushing the report at the end of the suite
 * ExtentReport.flushReports();
 * }</pre>
 *
 * @see ExtentReports
 * @see ExtentSparkReporter
 * @see ExtentManager
 * @version 1.0
 * @since 2023-10-27
 */
public final class ExtentReport {
    private static ExtentReports extentReports;

    private ExtentReport() {
    }

    /**
     * Initializes the ExtentReports instance with the specified configuration. It sets up the
     * path for the report, configures the theme, and attaches the spark reporter. It also
     * creates a separate report for failed tests.
     *
     * @param classname The name of the class or suite for which the report is being generated.
     */
    public static void initReports(String classname) {
        if (Objects.isNull(extentReports)) {
            extentReports = new ExtentReports();
            FrameworkConstants.setReportClassName(classname);
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(getReportPath()).viewConfigurer()
                    .viewOrder().as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST}).apply();
            ExtentSparkReporter failedSparkReporter = new ExtentSparkReporter(getServiceName().toLowerCase() + "_failed_testcase.html").filter().statusFilter().as(new Status[]{Status.FAIL}).apply();
            failedSparkReporter.config().setDocumentTitle(getServiceName().toUpperCase() + " Service Automation Failed Test Case");
            extentReports.attachReporter(sparkReporter, failedSparkReporter);
            sparkReporter.config().setTheme(Theme.STANDARD);
            failedSparkReporter.config().setTheme(Theme.STANDARD);
            failedSparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setDocumentTitle(classname + "_Automation Report");
            sparkReporter.config().setReportName(getServiceName().toUpperCase() + " Service Automation Testing Report");
            log(INFO, "Report file location is" + getReportPath());
        }
    }

    /**
     * Flushes the ExtentReports instance, which writes all the logs to the report file.
     * It also unloads the {@link ExtentManager} to clean up the thread-local variable.
     *
     * @throws IOException If there is an error while browsing the report file.
     */
    public static void flushReports() throws IOException {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        ExtentManager.unloadExtentTest();
    }

    /**
     * Creates a new test entry in the Extent Report with the given test case name.
     * It uses {@link ExtentManager} to ensure thread safety.
     *
     * @param testcasename The name of the test case to be created in the report.
     */
    public static void createTest(String testcasename) {
        ExtentManager.setExtentTest(extentReports.createTest(testcasename));
    }

    /**
     * Adds the specified authors to the current test in the Extent Report.
     *
     * @param authors An array of author names to be added to the test.
     */
    public static void addAuthors(String[] authors) {
        for (String temp : authors) {
            ExtentManager.getExtentTest().assignAuthor(temp);
        }
    }

    /**
     * Adds the specified categories to the current test in the Extent Report.
     *
     * @param categories An array of {@link CategoryType} enums to be added to the test.
     */
    public static void addCategories(CategoryType[] categories) {
        for (CategoryType temp : categories) {
            ExtentManager.getExtentTest().assignCategory(temp.toString());
        }
    }
}
