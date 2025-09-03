package org.op_ra.constants;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.utils.PropertyUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * {@code FrameworkConstants} contains all the constant values used within the testing framework.
 * This class is designed to be a central repository for static and final values, such as file paths,
 * report names, and other configuration details. By centralizing these constants, the framework
 * becomes easier to maintain and update.
 *
 * <p>This class is declared as {@code final} to prevent it from being extended, and its constructor
 * is private to prevent instantiation. All members of this class are static and should be accessed
 * directly using the class name.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To access a constant from this class, use the static getter methods provided. For example, to get
 * the path to the extent report, you can call {@code FrameworkConstants.getReportPath()}.</p>
 *
 * <pre>{@code
 * String reportPath = FrameworkConstants.getReportPath();
 * System.out.println("Report path: " + reportPath);
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public final class FrameworkConstants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FrameworkConstants() {
    }

    // Paths for different resources and report
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String SERVICE_NAME = "Atlas-Practice";
    private static final String TEST_RESOURCES_PATH = USER_DIR + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    private static final String CONFIG_FILE_PATH = TEST_RESOURCES_PATH + File.separator + "config";
    private static final String PAYLOAD_RESOURCES_PATH = TEST_RESOURCES_PATH + File.separator + "payload";

    // File paths for test data and configuration
    private static final String EXCEL_PATH = TEST_RESOURCES_PATH + File.separator + "testdata" + File.separator + "excel" + File.separator + "testdata.xlsx";
    private static String TESTDATAJSONFILEPATH = TEST_RESOURCES_PATH + File.separator + "testdata" + File.separator + "json";
    private static final String DATABASE_PATH = TEST_RESOURCES_PATH + File.separator + "testdata" + File.separator + "database" + File.separator + "inputDB.db";
    private static final String MAIN_RESOURCES_PATH = USER_DIR + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    private static final String TESTCASE_JSON_PATH = MAIN_RESOURCES_PATH + File.separator + "runnerlist" + File.separator + "runmanager.json";

    // Other constants
    private static final String SELECTQUERY_JSON_FILEPATH = MAIN_RESOURCES_PATH + File.separator + "configuration" + File.separator + "SqlQueries.json";
    private static final String DBConfig_JSON_FILEPATH = MAIN_RESOURCES_PATH + File.separator + "configuration" + File.separator + "DataBaseConfig.json";
    private static final String CLOUD_DB_CONFIG_JSON_FILE_PATH = MAIN_RESOURCES_PATH + File.separator + "configuration" + File.separator + "CloudDatabaseConfig.json";
    private static final String MailConfig_FILEPATH = MAIN_RESOURCES_PATH + File.separator + "configuration" + File.separator + "emailconfig.properties";
    private static final String EXTENT_REPORT_PATH = USER_DIR + File.separator + "extent-test-output";
    private static final String RESPONSE_OUTPUT_PATH = USER_DIR + File.separator + "response-test-output" + File.separator;
    private static final String EXCEL_REPORT_PATH = USER_DIR + File.separator + "excel-test-report";

    // Date and time formatting for reports
    private static final Date CURRENT_DATE = new Date();
    private static final String TODAY_DATE_TIME = new SimpleDateFormat("MMM-dd-yyyy_HH_mm_ss_SSS").format(CURRENT_DATE);

    // Sheet names in Excel files
    private static final String DATA_EXCEL_SHEET = "DATA";
    private static final String RUNMANGER_EXCEL_SHEET = "RUNMANAGER";
    private static final String RUNMANAGER = "RunManager";
    private static final String TESTCASE_LIST = "testCaseLists";
    private static boolean flag = false;
    private static String reportClassName = "";
    private static String reportFilePath = "";
    private static String ENVIRONMENT = PropertyUtils.getValue(ConfigProperties.ENV);
    private static String branchName = System.getenv("CI_COMMIT_REF_NAME") != null ? System.getenv("CI_COMMIT_REF_NAME") : "local";
    private static long MaxLimit = 2000L;


    /**
     * Returns the name of the run manager Excel sheet.
     *
     * @return The name of the run manager Excel sheet.
     */
    public static String getRunmangerExcelSheet() {
        return RUNMANGER_EXCEL_SHEET;
    }

    /**
     * Returns the file path of the test data Excel file.
     *
     * @return The file path of the test data Excel file.
     */
    public static String getExcelFilePath() {
        return EXCEL_PATH;
    }

    /**
     * Returns the current date and time formatted as a string.
     * The format is "MMM-dd-yyyy_HH_mm_ss_SSS".
     *
     * @return The formatted date and time string.
     */
    public static String getTodayDateTime() {
        return TODAY_DATE_TIME;
    }

    /**
     * Returns the file path of the test case JSON file.
     *
     * @return The file path of the test case JSON file.
     */
    public static String getTestCaseJsonPath() {
        return TESTCASE_JSON_PATH;
    }

    /**
     * Returns the key for the test case list in the run manager JSON.
     *
     * @return The key for the test case list.
     */
    public static String getTestcaselist() {
        return TESTCASE_LIST;
    }

    /**
     * Returns the name of the service under test.
     *
     * @return The name of the service.
     */
    public static String getServiceName() {
        return SERVICE_NAME;
    }

    /**
     * Returns the key for the run manager in the run manager JSON.
     *
     * @return The key for the run manager.
     */
    public static String getRunmanager() {
        return RUNMANAGER;
    }

    /**
     * Returns the maximum limit value.
     *
     * @return The maximum limit value.
     */
    public static long getMaxLimit() {
        return MaxLimit;
    }

    /**
     * Creates the file path for the Extent Report.
     * The path is determined by the "overridereports" property in the config file.
     *
     * @return The file path for the Extent Report.
     */
    private static String createReportPath() {
        try {
            if (PropertyUtils.getValue(ConfigProperties.OVERRIDEREPORTS).equalsIgnoreCase("NO")) {
                return EXTENT_REPORT_PATH + File.separator + getTodayDateTime() + File.separator + reportClassName + "_Automation Report" + ".html";
            } else {
                return EXTENT_REPORT_PATH + File.separator + getServiceName() + "_Service.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Returns the file path of the SQL queries JSON file.
     *
     * @return The file path of the SQL queries JSON file.
     */
    public static String getSqlQueryjsonfilepath() {
        return SELECTQUERY_JSON_FILEPATH;
    }

    /**
     * Returns the file path of the local database file.
     *
     * @return The file path of the local database file.
     */
    public static String getDatabasePath() {
        return DATABASE_PATH;
    }

    /**
     * Returns the output path for the API response.
     * The file extension is determined by the response type.
     *
     * @param endpointName The name of the endpoint.
     * @param responseType The type of the response (e.g., JSON, XML).
     * @return The output path for the API response.
     */
    public static String getResponseOutputPath(String endpointName, String responseType) {
        try {
            String fileExtension;
            if (responseType.equalsIgnoreCase("JSON")) {
                fileExtension = "json";
            } else if (responseType.equalsIgnoreCase("XML")) {
                fileExtension = "xml";
            } else {
                fileExtension = "txt";
            }

            return RESPONSE_OUTPUT_PATH + endpointName + "." + fileExtension;
        } catch (Exception e) {
            e.printStackTrace();
            return RESPONSE_OUTPUT_PATH + endpointName + ".txt";
        }
    }

    /**
     * Returns the file path of the database configuration JSON file.
     *
     * @return The file path of the database configuration JSON file.
     */
    public static String getDBConfigJSONPath() {
        return DBConfig_JSON_FILEPATH;
    }

    /**
     * Returns the file path of the cloud database configuration JSON file.
     *
     * @return The file path of the cloud database configuration JSON file.
     */
    public static String getCloudDbConfigJsonFilePath() {
        return CLOUD_DB_CONFIG_JSON_FILE_PATH;
    }

    /**
     * Returns the file path of the email configuration properties file.
     *
     * @return The file path of the email configuration properties file.
     */
    public static String getMailConfig() {
        return MailConfig_FILEPATH;
    }

    /**
     * Creates the file path for the Excel report.
     * The path is determined by the "overridereports" property in the config file.
     *
     * @return The file path for the Excel report.
     */
    public static String createExcelReportPath() {
        try {
            if (PropertyUtils.getValue(ConfigProperties.OVERRIDEREPORTS).equalsIgnoreCase("Yes")) {
                String path = EXCEL_REPORT_PATH + File.separator + getTodayDateTime() + "_Automation_Report.xlsx";
                System.out.println(path);
                return path;
            } else {
                return EXCEL_REPORT_PATH + File.separator + "Automation_Report.xlsx";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the name of the class for the report.
     *
     * @param reportClassName The name of the class.
     */
    public static void setReportClassName(String reportClassName) {
        FrameworkConstants.reportClassName = reportClassName;
    }

    /**
     * Returns the file path of the configuration properties file.
     *
     * @return The file path of the configuration properties file.
     */
    public static String getConfigFilePath() {
        return CONFIG_FILE_PATH + File.separator + "atlas_config.properties";
    }

    /**
     * Returns the file path of the Extent Report.
     * If the path is not already created, it will be created.
     *
     * @return The file path of the Extent Report.
     */
    public static String getReportPath() {
        if (reportFilePath.isEmpty()) {
            reportFilePath = createReportPath();
        }
        return reportFilePath;
    }

    /**
     * Returns the file path of the test data JSON file.
     *
     * @return The file path of the test data JSON file.
     */
    public static String getTestDataJsonFilePath() {
        return TESTDATAJSONFILEPATH;
    }

    /**
     * Sets the file path for the test data JSON file based on the environment name.
     * This method ensures that the path is set only once.
     *
     * @param environmentName The name of the environment (e.g., QA, UAT).
     */
    public static void setTestDataJsonFilePath(String environmentName) {
        if (!flag) {
            if (Objects.isNull(environmentName))
                TESTDATAJSONFILEPATH = TESTDATAJSONFILEPATH + "/" + environmentName.toUpperCase() + "_TestData.json";
            else
                TESTDATAJSONFILEPATH = TESTDATAJSONFILEPATH + "/" + getEnvironment().toUpperCase() + "_TestData.json";
            flag = true;
        }
    }

    /**
     * Returns the name of the data Excel sheet.
     *
     * @return The name of the data Excel sheet.
     */
    public static String getDataExcelSheet() {
        return DATA_EXCEL_SHEET;
    }

    /**
     * Returns the current environment name.
     *
     * @return The current environment name.
     */
    public static String getEnvironment() {
        return ENVIRONMENT;
    }

    /**
     * Returns the name of the current Git branch.
     * It returns "local" if the branch name is not one of "main", "dev-qe", or "dry-run".
     *
     * @return The name of the current Git branch.
     */
    public static String getBranchName() {
        String result;
        switch (branchName) {
            case "main":
                result = "main";
                break;
            case "dev-qe":
                result = "dev-qe";
                break;
            case "dry-run":
                result = "dry-run";
                break;
            default:
                result = "local";
                break;
        }
        return result;
    }

    /**
     * Sets the environment name.
     *
     * @param environment The name of the environment.
     */
    public static void setEnvironment(String environment) {
        FrameworkConstants.ENVIRONMENT = environment;
    }

    /**
     * Returns the file path of a document payload.
     *
     * @param fileName The name of the document file.
     * @return The file path of the document payload.
     */
    public static String getDocumentPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "docs" + File.separator + fileName;
    }

    /**
     * Returns the file path of a JSON payload.
     *
     * @param fileName The name of the JSON file.
     * @return The file path of the JSON payload.
     */
    public static String getJsonPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "json" + File.separator + "body" + File.separator + fileName;
    }

    /**
     * Returns the file path of a temporary JSON payload.
     *
     * @param fileName The name of the temporary JSON file.
     * @return The file path of the temporary JSON payload.
     */
    public static String getJsonTempPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "json" + File.separator + "temp" + File.separator + fileName;
    }

    /**
     * Returns the file path of a JSON response payload.
     *
     * @param fileName The name of the JSON response file.
     * @return The file path of the JSON response payload.
     */
    public static String getJsonResPayload(String fileName) {
        return USER_DIR + File.separator + "response-test-output" + File.separator + fileName;
    }

    /**
     * Returns the file path of an image payload.
     *
     * @param fileName The name of the image file.
     * @return The file path of the image payload.
     */
    public static String getImagePayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "image" + File.separator + fileName;
    }

    /**
     * Returns the file path of an XML payload.
     *
     * @param fileName The name of the XML file.
     * @return The file path of the XML payload.
     */
    public static String getXmlPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "xml" + File.separator + fileName;
    }

    /**
     * Returns the file path of an HTML payload.
     *
     * @param fileName The name of the HTML file.
     * @return The file path of the HTML payload.
     */
    public static String getHtmlPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "html" + File.separator + fileName;
    }
}
