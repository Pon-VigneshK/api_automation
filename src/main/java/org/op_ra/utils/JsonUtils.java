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
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class JsonUtils {
    private static final LinkedHashMap<String, LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testDataHashMap = new LinkedHashMap();
    private static final List<Map<String, Object>> finalDatalist = new ArrayList<>();
    private static final LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalMap = new LinkedHashMap();
    //test case runner list map
    private static final LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalTestList = new LinkedHashMap();
    private static final LinkedHashMap<String, LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testRunnerHashMap = new LinkedHashMap();
    // HashMap to store test runner data.
    private static HashMap<String, Object> queriesList = new HashMap();
    private static FileInputStream fileInputStream;
    private static String envName;

    private JsonUtils() {
    }


    /**
     * Get test details from the JSON file based on high-level and nested key names.
     *
     * @param highLevelKeyName The high-level key name in the JSON structure.
     * @param nestedKeyName    The nested key name in the JSON structure.
     * @return List of test details as maps.
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

    public static void updateLocationPayload(String input, String output) {
        File inputFile = new File(input);
        File outputFile = new File(output);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root;

        try {
            // Read JSON into ObjectNode
            root = (ObjectNode) objectMapper.readTree(inputFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input JSON file", e);
        }

        // Set a random localId > 200
        int localId = ThreadLocalRandom.current().nextInt(200, 2000); // Random between 201–9999
        root.put("localId", String.valueOf(localId));

        try {
            // Write the updated JSON to output file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write output JSON file", e);
        }
    }

}

