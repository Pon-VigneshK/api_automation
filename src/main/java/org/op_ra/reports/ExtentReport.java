package org.op_ra.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.CategoryType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static org.op_ra.constants.FrameworkConstants.getReportPath;
import static org.op_ra.constants.FrameworkConstants.getServiceName;
import static org.op_ra.enums.LogType.INFO;
import static org.op_ra.reports.FrameworkLogger.log;

/**
 * Manages the creation and configuration of ExtentReports.
 * This class provides methods to initialize, flush, and create test entries in the report.
 * It ensures thread safety for report generation using {@link ExtentManager}.
 */
public final class ExtentReport {
    private static ExtentReports extentReports;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ExtentReport() {
        // Private constructor
    }

    /**
     * Initializes the ExtentReports instance and configures the reporters (SparkReporter for main report,
     * and a separate SparkReporter for failed tests).
     * Sets up report document title, report name, theme, and view order.
     * This method should be called once before any tests start, usually from a TestNG suite listener.
     *
     * @param classname The name of the test class or suite, used for the report title and path.
     *                  Parameterized from the listener class.
     */
    public static void initReports(String classname) {
        if (Objects.isNull(extentReports)) {
            extentReports = new ExtentReports();
            FrameworkConstants.setReportClassName(classname); // Store classname for report path generation

            // Main report configuration
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(getReportPath())
                    .viewConfigurer()
                    .viewOrder()
                    .as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST})
                    .apply();
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setDocumentTitle(classname + "_Automation Report");
            sparkReporter.config().setReportName(getServiceName().toUpperCase() + " Service Automation Testing Report");

            // Failed test cases report configuration
            ExtentSparkReporter failedSparkReporter = new ExtentSparkReporter(getServiceName().toLowerCase() + "_failed_testcase.html")
                    .filter()
                    .statusFilter()
                    .as(new Status[]{Status.FAIL})
                    .apply();
            failedSparkReporter.config().setDocumentTitle(getServiceName().toUpperCase() + " Service Automation Failed Test Case");
            failedSparkReporter.config().setTheme(Theme.STANDARD);
            failedSparkReporter.config().setEncoding("utf-8");

            extentReports.attachReporter(sparkReporter, failedSparkReporter);
            log(INFO, "Report file location is " + getReportPath());
        }
    }

    /**
     * Flushes the ExtentReports instance, writing all logs to the report file.
     * Opens the generated HTML report in the default desktop browser.
     * Unloads the {@link ExtentManager} to clear the current test from thread-local storage.
     * This method should be called once after all tests in a suite have finished.
     *
     * @throws IOException If an error occurs while opening the report file.
     */
    public static void flushReports() throws IOException {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        ExtentManager.unloadExtentTest(); // Clear thread-local test instance
        // Open the report in the default browser
        File reportFile = new File(getReportPath());
        if (reportFile.exists()) {
            Desktop.getDesktop().browse(reportFile.toURI());
        } else {
            log(INFO, "Report file not found at: " + getReportPath());
        }
    }

    /**
     * Creates a new test node in the Extent report.
     * Delegates to {@link ExtentManager#setExtentTest(com.aventstack.extentreports.ExtentTest)}
     * to ensure thread safety when managing {@link com.aventstack.extentreports.ExtentTest} instances.
     *
     * @param testcasename The name of the test case to be reflected in the report.
     */
    public static void createTest(String testcasename) {
        if (Objects.nonNull(extentReports)) {
            ExtentManager.setExtentTest(extentReports.createTest(testcasename));
        } else {
            // Log an error or throw an exception if extentReports is not initialized
            log(INFO, "ExtentReports is not initialized. Cannot create test: " + testcasename);
        }
    }

    /**
     * Assigns authors to the current test in the Extent report.
     *
     * @param authors An array of author names.
     */
    public static void addAuthors(String[] authors) {
        if (Objects.nonNull(ExtentManager.getExtentTest())) {
            for (String temp : authors) {
                ExtentManager.getExtentTest().assignAuthor(temp);
            }
        }
    }

    /**
     * Assigns categories to the current test in the Extent report.
     *
     * @param categories An array of {@link CategoryType} enums.
     */
    public static void addCategories(CategoryType[] categories) {
        if (Objects.nonNull(ExtentManager.getExtentTest())) {
            for (CategoryType temp : categories) {
                ExtentManager.getExtentTest().assignCategory(temp.toString()); // Corrected method to assignCategory
            }
        }
    }
}
