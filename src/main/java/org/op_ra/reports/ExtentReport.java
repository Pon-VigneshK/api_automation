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

public final class ExtentReport {
    private static ExtentReports extentReports;

    private ExtentReport() {

    }

    /**
     * Sets the initial configuration for the Extent Reports and determines the report generation path.
     *
     * @param classname Parameterized from the listener class.
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
     * Flushing the reports ensures that extent logs are reflected properly.
     * Opens the report in the default desktop browser.
     * Sets the ThreadLocal variable to the default value.
     */
    public static void flushReports() throws IOException {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
        }
        ExtentManager.unloadExtentTest();
        Desktop.getDesktop().browse(new File(getReportPath()).toURI());
    }

    /**
     * Creates a test node in the Extent report. Delegates to {@link ExtentManager} for providing thread safety.
     *
     * @param testcasename Test Name that needs to be reflected in the report.
     */
    public static void createTest(String testcasename) {
        ExtentManager.setExtentTest(extentReports.createTest(testcasename));
    }

    public static void addAuthors(String[] authors) {
        for (String temp : authors) {
            ExtentManager.getExtentTest().assignAuthor(temp);
        }
    }

    public static void addCategories(CategoryType[] categories) {

        for (CategoryType temp : categories) {
            ExtentManager.getExtentTest().assignAuthor(temp.toString());
        }

    }

}
