package org.op_ra.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.ConfigProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * {@code JsonUtils} is a final class that provides utility methods for working with JSON data.
 * This class includes methods for reading test data from JSON files, generating JSON data from
 * a database, and updating JSON payloads.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to perform various JSON operations.</p>
 *
 * <pre>{@code
 * // Getting test details from a JSON file
 * List<Map<String, Object>> testDetails = JsonUtils.getTestDetails("runmanager", "testcaselist");
 *
 * // Generating test data from a database
 * JsonUtils.generateTestDataJson();
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class JsonUtils {
    private static final LinkedHashMap<String, LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testDataHashMap = new LinkedHashMap();
    private static final List<Map<String, Object>> finalDatalist = new ArrayList<>();
    private static final LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalMap = new LinkedHashMap();
    private static final LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalTestList = new LinkedHashMap();
    private static final LinkedHashMap<String, LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testRunnerHashMap = new LinkedHashMap();
    private static HashMap<String, Object> queriesList = new HashMap();
    private static FileInputStream fileInputStream;
    private static String envName;

    private JsonUtils() {
    }

    /**
     * Gets test details from a JSON file based on high-level and nested key names.
     *
     * @param highLevelKeyName The high-level key name in the JSON structure.
     * @param nestedKeyName    The nested key name in the JSON structure.
     * @return A list of maps, where each map represents a set of test details.
     */
    public static List<Map<String, Object>> getTestDetails(String highLevelKeyName, String nestedKeyName) {
        List<Map<String, Object>> testDetailsList = new ArrayList<>();
        FileInputStream fileInputStream = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            fileInputStream = new FileInputStream(FrameworkConstants.getTestCaseJsonPath());
            HashMap<String, HashMap<String, List<Map<String, Object>>>> testCaseMap =
                    objectMapper.readValue(fileInputStream, HashMap.class);
            List<Map<String, Object>> jsonTestCaseList = testCaseMap.get(highLevelKeyName).get(nestedKeyName);
            for (Map<String, Object> testCase : jsonTestCaseList) {
                testDetailsList.add(testCase);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(fileInputStream)) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return testDetailsList;
    }

    /**
     * Gets test data details from the test data JSON file.
     *
     * @return A list of maps, where each map represents a set of test data.
     */
    public static List<Map<String, Object>> getTestDataDetails() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            fileInputStream = new FileInputStream(FrameworkConstants.getTestDataJsonFilePath());
            HashMap<String, HashMap> jsonTestDataMap = objectMapper.readValue(fileInputStream, HashMap.class);
            System.out.println(jsonTestDataMap);
            LinkedHashMap<String, Object> jsonTestDataLinkedMap = (LinkedHashMap) jsonTestDataMap.get(FrameworkConstants.getEnvironment());
            try {
                for (Map.Entry<String, Object> finaltestcasemap : jsonTestDataLinkedMap.entrySet()) {
                    finalDatalist.addAll((ArrayList) finaltestcasemap.getValue());
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (Objects.nonNull(fileInputStream)) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return finalDatalist;
    }

    /**
     * Gets query details from the SQL queries JSON file.
     *
     * @param queryType The type of query to retrieve (e.g., "select", "runnerlist").
     * @return A map of query details.
     */
    public static HashMap<String, Object> getQueryDetails(String queryType) {

        try {
            String keyname = queryType.toLowerCase() + "queries";
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            fileInputStream = new FileInputStream(FrameworkConstants.getSqlQueryjsonfilepath());
            HashMap<String, Object> jsontestcasemap = objectMapper.readValue(fileInputStream, HashMap.class);
            queriesList = (HashMap<String, Object>) jsontestcasemap.get(keyname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (Objects.nonNull(fileInputStream)) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return queriesList;
    }

    /**
     * Generates a JSON file with test data from a database.
     */
    public static void generateTestDataJson() {
        try {

            Statement st = DataBaseConnectionUtils.getMyConn().createStatement();
            HashMap<String, Object> queryDetails = getQueryDetails("select");

            for (Map.Entry<String, Object> mapdata : queryDetails.entrySet()) {

                ResultSet resultSet = st.executeQuery((String) mapdata.getValue());
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columns = resultSetMetaData.getColumnCount();
                HashMap<String, Object> rowDatas;
                ArrayList<HashMap<String, Object>> testDataList = new ArrayList();

                while (resultSet.next()) {
                    rowDatas = new HashMap(columns);
                    for (int i = 1; i <= columns; ++i) {
                        rowDatas.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                        envName = PropertyUtils.getValue(ConfigProperties.ENV);
                    }
                    testDataList.add(rowDatas);
                }
                System.out.println("env name is:" + envName);
                finalMap.put(mapdata.getKey(), testDataList);
            }
            testDataHashMap.put(envName, finalMap);
            FrameworkConstants.setEnvironment(envName);

            ObjectMapper mapper = new ObjectMapper();
            FrameworkConstants.setTestDataJsonFilePath(envName);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FrameworkConstants.getTestDataJsonFilePath()), testDataHashMap);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(DataBaseConnectionUtils.getMyConn())) {
                try {
                    DataBaseConnectionUtils.getMyConn().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Generates a JSON file with the test runner list from a database.
     */
    public static void generateRunnerListJsonData() {
        try {
            Statement st = DataBaseConnectionUtils.getMyConn().createStatement();
            HashMap<String, Object> queryDetails = getQueryDetails("runnerlist");
            for (Map.Entry<String, Object> mapdata : queryDetails.entrySet()) {
                ResultSet resultSet = st.executeQuery((String) mapdata.getValue());
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columns = resultSetMetaData.getColumnCount();
                HashMap<String, Object> rowDatas;
                ArrayList<HashMap<String, Object>> testDataList = new ArrayList();
                while (resultSet.next()) {
                    rowDatas = new HashMap(columns);
                    for (int i = 1; i <= columns; ++i) {
                        rowDatas.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                    }
                    testDataList.add(rowDatas);
                }
                finalTestList.put(mapdata.getKey(), testDataList);
            }
            testRunnerHashMap.put(FrameworkConstants.getRunmanager(), finalTestList);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FrameworkConstants.getTestCaseJsonPath()), testRunnerHashMap);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();

        } catch (JsonGenerationException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a JSON payload with a random local ID and writes it to an output file.
     *
     * @param input The path to the input JSON file.
     * @param output The path to the output JSON file.
     */
    public static void updateLocationPayload(String input, String output) {
        File inputFile = new File(input);
        File outputFile = new File(output);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root;

        try {
            root = (ObjectNode) objectMapper.readTree(inputFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input JSON file", e);
        }

        int localId = ThreadLocalRandom.current().nextInt(200, 2000);
        root.put("localId", String.valueOf(localId));

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write output JSON file", e);
        }
    }
}
