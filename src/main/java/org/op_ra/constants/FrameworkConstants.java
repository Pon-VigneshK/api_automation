package org.op_ra.constants;

import lombok.Getter; // Assuming Lombok is used for getters as per typical modern Java projects
import org.op_ra.enums.ConfigProperties;
import org.op_ra.utils.PropertyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Defines framework-level constants, such as file paths, timeouts, and report configurations.
 * This class helps in managing static configuration data centrally.
 * <p>
 * Many paths are constructed dynamically based on the project structure and current date/time.
 * It uses {@link PropertyUtils} to fetch base paths or environment-specific details from configuration files.
 * </p>
 */
public final class FrameworkConstants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FrameworkConstants() {
        // Private constructor
    }

    // Base Paths - Consider making these configurable if they change often
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String RESOURCES_PATH = USER_DIR + "/src/test/resources";
    private static final String CONFIG_FILE_PATH = USER_DIR + "/src/main/resources/configuration/config.properties";
    private static final String JSON_CONFIG_FILE_PATH = RESOURCES_PATH + "/config/jsonConfig.json";
    private static final String EXCEL_FILE_PATH = RESOURCES_PATH + "/excel/api_test_data.xlsx"; // Example, make configurable
    private static final String EXTENT_REPORT_FOLDER_PATH = USER_DIR + "/extent-test-output/";
    private static String extentReportFilePath = ""; // Dynamically set

    // Test Data JSON paths - these might be environment specific
    private static final String SQL_QUERY_JSON_FILE_PATH = RESOURCES_PATH + "/testdata/SqlQuery.json";
    private static String testDataJsonFilePath = ""; // Dynamically set based on environment
    private static String testCaseJsonPath = "";     // Dynamically set based on run manager

    // Report and Runner details
    @Getter private static String runmanager; // Using Lombok @Getter, ensure dependency is present
    @Getter private static String environment;
    @Getter private static String reportClassName; // Name of the class/suite for report naming
    @Getter private static String serviceName; // Name of the service under test, from config

    // Static initializer block to load some values from properties or set defaults
    static {
        runmanager = PropertyUtils.getValue(ConfigProperties.RUNMANAGER); // Example
        environment = PropertyUtils.getValue(ConfigProperties.ENV);       // Example
        serviceName = PropertyUtils.getValue(ConfigProperties.SERVICE_NAME); // Example
        // Initialize paths that depend on runmanager or environment
        testCaseJsonPath = RESOURCES_PATH + "/testdata/" + runmanager + "_testcase.json";
        testDataJsonFilePath = RESOURCES_PATH + "/testdata/" + environment + "_testdata.json";
    }


    /**
     * Gets the file path for the main configuration file (config.properties).
     *
     * @return Absolute path to the config.properties file.
     */
    public static String getConfigFilePath() {
        return CONFIG_FILE_PATH;
    }

    /**
     * Gets the file path for the JSON configuration file (jsonConfig.json).
     *
     * @return Absolute path to the jsonConfig.json file.
     */
    public static String getJsonConfigFilePath() {
        return JSON_CONFIG_FILE_PATH;
    }

    /**
     * Gets the file path for the Excel test data file.
     * Note: The actual file name "api_test_data.xlsx" is hardcoded here.
     * Consider making this more dynamic or configurable if multiple Excel files are used.
     *
     * @return Absolute path to the Excel test data file.
     */
    public static String getExcelFilePath() {
        return EXCEL_FILE_PATH;
    }

    /**
     * Generates and returns the file path for the Extent HTML report.
     * The file name includes the {@link #reportClassName}, service name, and a timestamp.
     * Example: "ExtentReport_MyService_MyTestSuite_2023-10-27_10-30-00.html"
     * If the report path has already been generated for the current execution, it returns the existing path.
     *
     * @return Absolute path for the Extent report HTML file.
     */
    public static String getReportPath() {
        if (extentReportFilePath.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = sdf.format(new Date());
            // Ensure reportClassName and serviceName are not null or empty to prevent "null" in filename
            String rcn = reportClassName != null ? reportClassName.replaceAll("[^a-zA-Z0-9-_\.]", "_") : "DefaultSuite";
            String sn = serviceName != null ? serviceName.replaceAll("[^a-zA-Z0-9-_\.]", "_") : "DefaultService";

            extentReportFilePath = EXTENT_REPORT_FOLDER_PATH + "ExtentReport_" + sn + "_" + rcn + "_" + timestamp + ".html";
            java.io.File reportDir = new java.io.File(EXTENT_REPORT_FOLDER_PATH);
            if (!reportDir.exists()) {
                reportDir.mkdirs(); // Create the directory if it doesn'''t exist
            }
        }
        return extentReportFilePath;
    }

    /**
     * Sets the class/suite name to be used in the ExtentReport file name.
     * This is typically called from a listener when a suite or class starts.
     *
     * @param reportClassName The name of the test class or suite.
     */
    public static void setReportClassName(String reportClassName) {
        FrameworkConstants.reportClassName = reportClassName;
        // Reset extentReportFilePath so it gets regenerated with the new class name
        FrameworkConstants.extentReportFilePath = "";
    }

    /**
     * Gets the file path for the SQL query JSON file (SqlQuery.json).
     *
     * @return Absolute path to the SqlQuery.json file.
     */
    public static String getSqlQueryjsonfilepath() {
        return SQL_QUERY_JSON_FILE_PATH;
    }

    /**
     * Gets the file path for the environment-specific test data JSON file.
     * The file name is constructed using the current {@link #environment}.
     * Example: "DEV_testdata.json"
     *
     * @return Absolute path to the environment-specific test data JSON file.
     */
    public static String getTestDataJsonFilePath() {
        // Ensure environment is set, otherwise default or throw error
        if (environment == null || environment.trim().isEmpty()) {
            System.err.println("Environment is not set in FrameworkConstants. Defaulting test data path or this could be an error.");
            // return RESOURCES_PATH + "/testdata/default_testdata.json"; // Or throw
        }
        return testDataJsonFilePath; // Already constructed in static block
    }

    /**
     * Sets the file path for the environment-specific test data JSON.
     * This might be called if the environment changes or needs to be set programmatically.
     *
     * @param environmentName The name of the environment (e.g., "DEV", "QA").
     */
    public static void setTestDataJsonFilePath(String environmentName) {
        if (environmentName != null && !environmentName.trim().isEmpty()) {
            FrameworkConstants.environment = environmentName; // Update current environment
            FrameworkConstants.testDataJsonFilePath = RESOURCES_PATH + "/testdata/" + environmentName + "_testdata.json";
        } else {
             System.err.println("Attempted to set test data JSON path with null or empty environment name.");
        }
    }

    /**
     * Gets the file path for the run manager-specific test case JSON file.
     * The file name is constructed using the current {@link #runmanager}.
     * Example: "SmokeTests_testcase.json"
     *
     * @return Absolute path to the run manager-specific test case JSON file.
     */
    public static String getTestCaseJsonPath() {
         // Ensure runmanager is set
        if (runmanager == null || runmanager.trim().isEmpty()) {
            System.err.println("Runmanager is not set in FrameworkConstants. Defaulting test case path or this could be an error.");
            // return RESOURCES_PATH + "/testdata/default_testcase.json"; // Or throw
        }
        return testCaseJsonPath; // Already constructed in static block
    }

    /**
     * Sets the run manager name. This also updates the {@link #testCaseJsonPath}.
     *
     * @param runmanager The name of the run manager (e.g., "SmokeTests", "RegressionSuite").
     */
    public static void setRunmanager(String runmanager) {
        if (runmanager != null && !runmanager.trim().isEmpty()) {
            FrameworkConstants.runmanager = runmanager;
            FrameworkConstants.testCaseJsonPath = RESOURCES_PATH + "/testdata/" + runmanager + "_testcase.json";
        } else {
            System.err.println("Attempted to set run manager with null or empty name.");
        }
    }

    /**
     * Sets the environment name. This also updates the {@link #testDataJsonFilePath}.
     *
     * @param environment The name of the environment (e.g., "DEV", "QA").
     */
    public static void setEnvironment(String environment) {
         if (environment != null && !environment.trim().isEmpty()) {
            FrameworkConstants.environment = environment;
            FrameworkConstants.testDataJsonFilePath = RESOURCES_PATH + "/testdata/" + environment + "_testdata.json";
        } else {
            System.err.println("Attempted to set environment with null or empty name.");
        }
    }

    // Default timeout values (consider making these configurable via properties file)
    /** Default explicit wait timeout in seconds. */
    public static final int EXPLICIT_WAIT_TIMEOUT = 10;
    /** Default timeout for page loads in seconds. */
    public static final int PAGE_LOAD_TIMEOUT = 30;
    /** Default sleep/pause duration in milliseconds. */
    public static final int SHORT_SLEEP_DURATION = 1000; // 1 second

}
