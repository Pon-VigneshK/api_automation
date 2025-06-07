package org.op_ra.constants;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.utils.PropertyUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public final class FrameworkConstants {

    /**
     * FrameworkConstants holds all the constant values used within the framework. If some value needs to be changed
     * or modified often, then it should be stored in the property files.
     */

    // Paths for different resources and report

    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String SERVICE_NAME = "OPEN";
    private static final long MAX_LIMIT = 2000L;
    //System.getProperty("service.name");
    private static final String TEST_RESOURCES_PATH = USER_DIR + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    private static final String CONFIG_FILE_PATH = TEST_RESOURCES_PATH + File.separator + "config";
    private static final String PAYLOAD_RESOURCES_PATH = TEST_RESOURCES_PATH + File.separator + "payload";
    // File paths for test data and configuration //
    private static final String EXCEL_PATH = TEST_RESOURCES_PATH + File.separator + "testdata" + File.separator + "excel" + File.separator + "testdata.xlsx";
    private static String TESTDATAJSONFILEPATH = TEST_RESOURCES_PATH + File.separator + "testdata" + File.separator + "json";
    private static final String DATABASE_PATH = TEST_RESOURCES_PATH + File.separator + "testdata" + File.separator + "database" + File.separator + "inputData.db";
    //private static String DATABASE_PATH = TEST_RESOURCES_PATH +/testdata/database/inputData.db;
    private static final String MAIN_RESOURCES_PATH = USER_DIR + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    private static final String TESTCASE_JSON_PATH = MAIN_RESOURCES_PATH + File.separator + "runnerlist" + File.separator + "runmanager.json";
    // Other constants
    private static final String SELECTQUERY_JSON_FILEPATH = MAIN_RESOURCES_PATH + File.separator + "configuration" + File.separator + "SqlQueries.json";
    private static final String DBConfig_JSON_FILEPATH = MAIN_RESOURCES_PATH + File.separator + "Configuration" + File.separator + "DataBaseConfig.json";
    private static final String MailConfig_FILEPATH = MAIN_RESOURCES_PATH + File.separator + "Configuration" + File.separator + "emailconfig.properties";
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
    private static final String ONE = "1";

    /**
     * Constants class for storing framework-related paths and values.
     */
    public static String getRunmangerExcelSheet() {
        return RUNMANGER_EXCEL_SHEET;
    }

    public static String getExcelFilePath() {
        return EXCEL_PATH;
    }

    public static String getTodayDateTime() {
        return TODAY_DATE_TIME;
    }

    public static String getTestCaseJsonPath() {
        return TESTCASE_JSON_PATH;
    }

    public static String getTestcaselist() {
        return TESTCASE_LIST;
    }

    public static long getMaxLimit(){
        return  MAX_LIMIT;
    }

    public static String getServiceName() {
        return SERVICE_NAME;
    }

    public static String getRunmanager() {
        return RUNMANAGER;
    }

    private static String createReportPath() {
        try {
            String reportPath = EXTENT_REPORT_PATH + File.separator + getTodayDateTime() + File.separator + reportClassName + "_Automation Report";
            if (PropertyUtils.getValue(ConfigProperties.OVERRIDE_REPORTS).equalsIgnoreCase("NO")) {
                return EXTENT_REPORT_PATH + File.separator + getTodayDateTime() + File.separator + reportClassName + "_Automation Report" + ".html";
            } else {
                return EXTENT_REPORT_PATH + File.separator + getServiceName() + "_Service.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSqlQueryjsonfilepath() {
        return SELECTQUERY_JSON_FILEPATH;
    }

    public static String getDatabasePath() {
        return DATABASE_PATH;
    }

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

    public static String getDBConfigJSONPath() {
        return DBConfig_JSON_FILEPATH;
    }

    public static String getMailConfig() {
        return MailConfig_FILEPATH;
    }

    public static String createExcelReportPath() {
        try {
            if (PropertyUtils.getValue(ConfigProperties.OVERRIDE_REPORTS).equalsIgnoreCase("Yes")) {
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

    public static void setReportClassName(String reportClassName) {
        FrameworkConstants.reportClassName = reportClassName;
    }

    public static String getConfigFilePath(String service) {
        return CONFIG_FILE_PATH + File.separator + service.toLowerCase() + "_config.properties";
    }

    public static String getReportPath() {
        if (reportFilePath.isEmpty()) {
            reportFilePath = createReportPath();
        }
        return reportFilePath;
    }

    public static String getTestDataJsonFilePath() {
        return TESTDATAJSONFILEPATH;
    }

    public static void setTestDataJsonFilePath(String environmentName) {
        if (!flag) {
            if (Objects.isNull(environmentName))
                TESTDATAJSONFILEPATH = TESTDATAJSONFILEPATH + "/" + environmentName.toUpperCase() + "_TestData.json";
            else
                TESTDATAJSONFILEPATH = TESTDATAJSONFILEPATH + "/" + getEnvironment().toUpperCase() + "_TestData.json";
            flag = true;
        }

    }

    public static String getDataExcelSheet() {
        return DATA_EXCEL_SHEET;
    }

    public static String getEnvironment() {
        return ENVIRONMENT;
    }

    public static void setEnvironment(String environment) {
        FrameworkConstants.ENVIRONMENT = environment;
    }

    public static String getDocumentPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "docs" + File.separator + fileName;
    }

    public static String getJsonPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "json" + File.separator + "body" + File.separator + fileName;
    }

    public static String getJsonTempPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "json" + File.separator + "temp" + File.separator + fileName;
    }

    public static String getJsonResPayload(String fileName) {
        return USER_DIR + File.separator + "response-test-output" + File.separator + fileName;
    }

    public static String getImagePayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "image" + File.separator + fileName;
    }

    public static String getXmlPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "xml" + File.separator + fileName;
    }

    public static String getHtmlPayload(String fileName) {
        return PAYLOAD_RESOURCES_PATH + File.separator + "html" + File.separator + fileName;
    }

    public static String getNumberOne() {
        return ONE;
    }
}
