package org.op_ra.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.op_ra.constants.FrameworkConstants;
import org.op_ra.enums.ConfigProperties;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utility class for handling JSON data and related operations.
 * This class provides methods for:
 * <ul>
 *     <li>Generating JSON test data from Excel sheets or databases.</li>
 *     <li>Reading test data and runner lists from JSON files.</li>
 *     <li>Fetching SQL queries from a JSON configuration.</li>
 *     <li>Generating and updating JSON payloads for API requests using template files and dynamic data.</li>
 *     <li>Converting Excel cell values to strings.</li>
 * </ul>
 * It uses Jackson and Gson libraries for JSON processing and Apache POI for Excel handling.
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"}) // Suppressing common warnings for utility class
public final class JsonUtils {
    // Maps to store test data and runner lists in memory, loaded from JSON files.
    private static final LinkedHashMap<String, LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testDataHashMap = new LinkedHashMap<>();
    private static final List<Map<String, Object>> finalDatalist = new ArrayList<>();
    private static final LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalMap = new LinkedHashMap<>();
    private static final LinkedHashMap<String, ArrayList<HashMap<String, Object>>> finalTestList = new LinkedHashMap<>();
    private static final LinkedHashMap<String, LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> testRunnerHashMap = new LinkedHashMap<>();
    private static HashMap<String, Object> queriesList = new HashMap<>();
    private static FileInputStream fileInputStream; // Reusable file input stream
    private static String envName; // Stores the current environment name

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private JsonUtils() {
        // Private constructor
    }

    /**
     * Reads data from a specified Excel sheet and writes it to a JSON file.
     * This is used to generate a JSON representation of the test runner list.
     * The output JSON file path is determined by {@link FrameworkConstants#getTestCaseJsonPath()}.
     *
     * @param sheetName The name of the Excel sheet to read data from.
     *                  The Excel file path is retrieved from {@link FrameworkConstants#getExcelFilePath()}.
     * @throws RuntimeException if there is an issue with file operations (e.g., file not found, IO error).
     */
    public static void generateRunnerListJsonDataFromExcel(String sheetName) {
        try (FileInputStream fs = new FileInputStream(FrameworkConstants.getExcelFilePath())) {
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet " + sheetName + " not found in " + FrameworkConstants.getExcelFilePath());
            }
            int lastrownum = sheet.getLastRowNum();
            int lastcolnum = sheet.getRow(0).getLastCellNum(); // Assumes header row exists at index 0

            HashMap<String, Object> map;
            ArrayList<HashMap<String, Object>> testDataList = new ArrayList<>();
            String testCaseListName = "testCaseLists"; // Key for the list of test cases in JSON

            for (int i = 1; i <= lastrownum; i++) { // Start from row 1 (assuming row 0 is header)
                map = new HashMap<>();
                for (int j = 0; j < lastcolnum; j++) {
                    String key = sheet.getRow(0).getCell(j).getStringCellValue();
                    String value = convertCellValueToString(sheet.getRow(i).getCell(j));
                    map.put(key, value);
                }
                testDataList.add(map);
            }
            finalTestList.put(testCaseListName, testDataList);
            testRunnerHashMap.put(FrameworkConstants.getRunmanager(), finalTestList); // Run manager as top-level key

            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FrameworkConstants.getTestCaseJsonPath()), testRunnerHashMap);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Excel file not found: " + FrameworkConstants.getExcelFilePath(), e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading or writing Excel/JSON file: " + e.getMessage(), e);
        }
    }

    /**
     * Converts an Excel cell'''s value to its string representation.
     * Handles different cell types: STRING, NUMERIC, BOOLEAN, FORMULA.
     * Empty cells or null cells will return an empty string.
     *
     * @param cell The Apache POI {@link Cell} object.
     * @return The string representation of the cell'''s value. Returns an empty string if the cell is null.
     */
    private static String convertCellValueToString(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                // Check if the numeric value is an integer
                if (numericValue == (long) numericValue) {
                    return String.valueOf((long) numericValue);
                } else {
                    return String.valueOf(numericValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // For formula cells, it attempts to evaluate and return the cached formula result type
                // For more complex formula evaluation, a FormulaEvaluator would be needed.
                return cell.getCellFormula(); // Or evaluate formula if needed: cell.getNumericCellValue(), etc.
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Retrieves a list of test details (as maps) from a JSON file.
     * The JSON file is expected to have a specific structure where test details are nested
     * under a high-level key (e.g., environment) and a nested key (e.g., test group).
     *
     * @param highLevelKeyName The primary key in the JSON structure (e.g., "runManagerName" or environment).
     * @param nestedKeyName    The secondary key under which the list of test cases is stored (e.g., "testCaseLists").
     * @return A list of {@code Map<String, Object>}, where each map represents a test case detail.
     *         Returns an empty list if the keys are not found or an error occurs.
     */
    public static List<Map<String, Object>> getTestDetails(String highLevelKeyName, String nestedKeyName) {
        List<Map<String, Object>> testDetailsList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Configure to ignore properties in JSON not present in the map, to prevent errors.
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            fis = new FileInputStream(FrameworkConstants.getTestCaseJsonPath());
            // Expecting a structure like: Map<String, Map<String, List<Map<String, Object>>>>
            HashMap<String, HashMap<String, List<Map<String, Object>>>> testCaseMap =
                    objectMapper.readValue(fis, HashMap.class);

            if (testCaseMap.containsKey(highLevelKeyName) &&
                testCaseMap.get(highLevelKeyName).containsKey(nestedKeyName)) {
                List<Map<String, Object>> jsonTestCaseList = testCaseMap.get(highLevelKeyName).get(nestedKeyName);
                if (jsonTestCaseList != null) {
                    testDetailsList.addAll(jsonTestCaseList);
                }
            } else {
                System.err.println("Keys not found in JSON: " + highLevelKeyName + " -> " + nestedKeyName);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Test case JSON file not found: " + FrameworkConstants.getTestCaseJsonPath() + " - " + e.getMessage());
        } catch (IOException e) { // Catching generic IOException for other read errors
            System.err.println("Error reading test case JSON file: " + e.getMessage());
        } finally {
            if (Objects.nonNull(fis)) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing file input stream: " + e.getMessage());
                }
            }
        }
        return testDetailsList;
    }

    /**
     * Retrieves detailed test data from a JSON file specific to the current environment.
     * The environment is determined by {@link FrameworkConstants#getEnvironment()}.
     * The JSON data is expected to be structured by environment, then by test case group/name.
     *
     * @return A list of {@code Map<String, Object>}, where each map represents a set of data for a test iteration.
     *         Returns an empty list if the file is not found or data for the environment is missing.
     */
    public static List<Map<String, Object>> getTestDataDetails() {
        finalDatalist.clear(); // Clear previous data before loading new
        FileInputStream fis = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            fis = new FileInputStream(FrameworkConstants.getTestDataJsonFilePath());

            // Expecting structure: Map<String_Env, Map<String_TestCaseGroup, List<Map<String, Object>>>>
            HashMap<String, LinkedHashMap<String, ArrayList<HashMap<String, Object>>>> jsonTestDataMap =
                    objectMapper.readValue(fis, HashMap.class);

            String currentEnvironment = FrameworkConstants.getEnvironment();
            if (jsonTestDataMap.containsKey(currentEnvironment)) {
                LinkedHashMap<String, ArrayList<HashMap<String, Object>>> envTestData = jsonTestDataMap.get(currentEnvironment);
                for (Map.Entry<String, ArrayList<HashMap<String, Object>>> entry : envTestData.entrySet()) {
                    finalDatalist.addAll(entry.getValue());
                }
            } else {
                System.err.println("No test data found for environment: " + currentEnvironment + " in " + FrameworkConstants.getTestDataJsonFilePath());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Test data JSON file not found: " + FrameworkConstants.getTestDataJsonFilePath() + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading test data JSON: " + e.getMessage());
        } finally {
            if (Objects.nonNull(fis)) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing file input stream for test data: " + e.getMessage());
                }
            }
        }
        return new ArrayList<>(finalDatalist); // Return a copy
    }

    /**
     * Retrieves SQL query strings from a JSON configuration file based on the query type (e.g., "select", "runnerlist").
     * The specific query file path is determined by {@link FrameworkConstants#getSqlQueryjsonfilepath()}.
     * The JSON is expected to have a top-level key like "selectqueries" or "runnerlistqueries".
     *
     * @param queryType The type of queries to retrieve (e.g., "select", "runnerlist"). This is used to construct the key in the JSON.
     * @return A {@code HashMap<String, Object>} where keys are query names and values are the SQL query strings.
     *         Returns an empty map if the file or keys are not found, or an error occurs.
     */
    public static HashMap<String, Object> getQueryDetails(String queryType) {
        queriesList.clear(); // Clear previous queries
        FileInputStream fis = null;
        try {
            String keyname = queryType.toLowerCase() + "queries";
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            fis = new FileInputStream(FrameworkConstants.getSqlQueryjsonfilepath());
            // Expecting structure: Map<String, Map<String, String>> -> e.g. {"selectqueries": {"queryName1": "SELECT * FROM table1", ...}}
            HashMap<String, HashMap<String, Object>> jsonQueryMap = objectMapper.readValue(fis, HashMap.class);

            if (jsonQueryMap.containsKey(keyname)) {
                queriesList = jsonQueryMap.get(keyname);
            } else {
                 System.err.println("No queries found for type: " + queryType + " (key: " + keyname + ") in " + FrameworkConstants.getSqlQueryjsonfilepath());
            }
        } catch (FileNotFoundException e) {
            System.err.println("SQL query JSON file not found: " + FrameworkConstants.getSqlQueryjsonfilepath() + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading SQL query JSON: " + e.getMessage());
        } finally {
            if (Objects.nonNull(fis)) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing file input stream for SQL queries: " + e.getMessage());
                }
            }
        }
        return new HashMap<>(queriesList); // Return a copy
    }

    /**
     * Generates test data by fetching it from a database and writing it to a JSON file.
     * SQL queries are retrieved using {@link #getQueryDetails(String)} with type "select".
     * The database connection is obtained from {@link DataBaseConnectionUtils#getMyConn()}.
     * The output JSON file path is determined by {@link FrameworkConstants#getTestDataJsonFilePath()}
     * and the current environment {@link FrameworkConstants#getEnvironment()}.
     *
     * @throws RuntimeException if there is an issue with database operations, JSON processing, or file I/O.
     */
    public static void generateTestDataJson() {
        try {
            Statement st = DataBaseConnectionUtils.getMyConn().createStatement();
            HashMap<String, Object> queryDetails = getQueryDetails("select"); // Get "select" type queries

            finalMap.clear(); // Clear previous data

            for (Map.Entry<String, Object> mapdata : queryDetails.entrySet()) {
                String queryKey = mapdata.getKey();
                String sqlQuery = (String) mapdata.getValue();
                ResultSet resultSet = st.executeQuery(sqlQuery);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columns = resultSetMetaData.getColumnCount();
                ArrayList<HashMap<String, Object>> testDataList = new ArrayList<>();

                while (resultSet.next()) {
                    HashMap<String, Object> rowDatas = new HashMap<>(columns);
                    for (int i = 1; i <= columns; ++i) {
                        rowDatas.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                    }
                    testDataList.add(rowDatas);
                }
                finalMap.put(queryKey, testDataList); // Store data list by query key
                resultSet.close(); // Close ResultSet
            }
            st.close(); // Close Statement

            envName = PropertyUtils.getValue(ConfigProperties.ENV); // Get current environment name
            FrameworkConstants.setEnvironment(envName); // Ensure FrameworkConstants has the latest env
            testDataHashMap.clear(); // Clear previous environment data
            testDataHashMap.put(envName, new LinkedHashMap<>(finalMap)); // Store current finalMap under envName

            ObjectMapper mapper = new ObjectMapper();
            FrameworkConstants.setTestDataJsonFilePath(envName); // Set JSON file path based on env
            File outputFile = new File(FrameworkConstants.getTestDataJsonFilePath());
            outputFile.getParentFile().mkdirs(); // Ensure directory exists
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, testDataHashMap);

        } catch (SQLException e) {
            throw new RuntimeException("SQL error during test data generation: " + e.getMessage(), e);
        } catch (IOException e) { // Catch IO exceptions for file writing
            throw new RuntimeException("File I/O error during test data JSON generation: " + e.getMessage(), e);
        } finally {
            // Ensure database connection is closed
            if (Objects.nonNull(DataBaseConnectionUtils.getMyConn())) {
                try {
                    DataBaseConnectionUtils.getMyConn().close();
                } catch (SQLException e) {
                    System.err.println("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Generates a JSON runner list by fetching data from a database.
     * SQL queries are retrieved using {@link #getQueryDetails(String)} with type "runnerlist".
     * The output JSON file path is determined by {@link FrameworkConstants#getTestCaseJsonPath()}.
     *
     * @throws RuntimeException if there is an issue with database operations, JSON processing, or file I/O.
     */
    public static void generateRunnerListJsonData() {
        try {
            Statement st = DataBaseConnectionUtils.getMyConn().createStatement();
            HashMap<String, Object> queryDetails = getQueryDetails("runnerlist");

            finalTestList.clear(); // Clear previous data

            for (Map.Entry<String, Object> mapdata : queryDetails.entrySet()) {
                String queryKey = mapdata.getKey();
                String sqlQuery = (String) mapdata.getValue();
                ResultSet resultSet = st.executeQuery(sqlQuery);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columns = resultSetMetaData.getColumnCount();
                ArrayList<HashMap<String, Object>> testDataList = new ArrayList<>();

                while (resultSet.next()) {
                    HashMap<String, Object> rowDatas = new HashMap<>(columns);
                    for (int i = 1; i <= columns; ++i) {
                        rowDatas.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
                    }
                    testDataList.add(rowDatas);
                }
                finalTestList.put(queryKey, testDataList);
                 resultSet.close(); // Close ResultSet
            }
            st.close(); // Close Statement

            testRunnerHashMap.clear();
            testRunnerHashMap.put(FrameworkConstants.getRunmanager(), new LinkedHashMap<>(finalTestList));

            ObjectMapper mapper = new ObjectMapper();
            File outputFile = new File(FrameworkConstants.getTestCaseJsonPath());
            outputFile.getParentFile().mkdirs(); // Ensure directory exists
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, testRunnerHashMap);

        } catch (SQLException e) {
            throw new RuntimeException("SQL error during runner list JSON generation: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("File I/O error during runner list JSON generation: " + e.getMessage(), e);
        } finally {
            // Ensure database connection is closed
            if (Objects.nonNull(DataBaseConnectionUtils.getMyConn())) {
                try {
                    DataBaseConnectionUtils.getMyConn().close();
                } catch (SQLException e) {
                     System.err.println("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Generates a JSON request payload by replacing placeholders in a template JSON file with provided data.
     * Uses Gson for parsing and generating JSON.
     *
     * @param inputFilePath  Path to the template JSON file.
     * @param outputFilePath Path to write the generated JSON payload file.
     * @param data           A {@code Map<String, Object>} containing key-value pairs for replacements.
     *                       Keys in the map should correspond to keys in the JSON template whose values need replacement.
     */
    public static void generatePayload(String inputFilePath, String outputFilePath, Map<String, Object> data) {
        try (FileReader fileReader = new FileReader(inputFilePath)) {
            JsonObject baseJsonData = JsonParser.parseReader(fileReader).getAsJsonObject();
            replaceValues(baseJsonData, data); // Helper method to recursively replace values
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(baseJsonData, fileWriter);
            } catch (IOException e) {
                System.err.println("Error writing generated payload to " + outputFilePath + ": " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input payload template not found: " + inputFilePath + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading input payload template " + inputFilePath + ": " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON syntax in " + inputFilePath + ": " + e.getMessage());
        }
    }

    /**
     * Generates a JSON request payload string by replacing placeholders in a template JSON file with provided data.
     * Similar to {@link #generatePayload(String, String, Map)} but returns the JSON as a string
     * and also writes it to the output file.
     *
     * @param inputFilePath  Path to the template JSON file.
     * @param outputFilePath Path to write the generated JSON payload file.
     * @param data           A {@code Map<String, Object>} containing key-value pairs for replacements.
     * @return The generated JSON payload as a String. Returns "error" if an IOException occurs.
     */
    public static String generatePayloadBDD(String inputFilePath, String outputFilePath, Map<String, Object> data) {
        try (FileReader fileReader = new FileReader(inputFilePath)) {
            JsonObject baseJsonData = JsonParser.parseReader(fileReader).getAsJsonObject();
            replaceValues(baseJsonData, data);
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(baseJsonData, fileWriter);
            } catch (IOException e) {
                System.err.println("Error writing generated BDD payload to " + outputFilePath + ": " + e.getMessage());
                return "error"; // Indicate error
            }
            return new String(Files.readAllBytes(Paths.get(outputFilePath))); // Read content back from the output file
        } catch (FileNotFoundException e) {
            System.err.println("Input BDD payload template not found: " + inputFilePath + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error processing BDD payload template " + inputFilePath + ": " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON syntax in BDD template " + inputFilePath + ": " + e.getMessage());
        }
        return "error"; // Indicate error
    }


    /**
     * Recursively replaces values in a JsonObject based on a replacements map.
     * This method iterates through the JSON object. If a key matches one in the {@code replacements} map,
     * its value is updated. It handles nested JsonObjects and JsonArrays.
     *
     * @param jsonObject   The {@link JsonObject} to modify.
     * @param replacements A map where keys are JSON keys (potentially dot-separated for nested paths, though current impl is direct key)
     *                     and values are the new values to set.
     */
    private static void replaceValues(JsonObject jsonObject, Map<String, Object> replacements) {
        for (Map.Entry<String, JsonElement> entry : new HashSet<>(jsonObject.entrySet())) { // Iterate over a copy to allow modification
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (replacements.containsKey(key)) { // Direct replacement if key exists in replacements map
                JsonElement replacementElement = new Gson().toJsonTree(replacements.get(key));
                jsonObject.add(key, replacementElement);
            } else if (value.isJsonObject()) { // Recursively call for nested objects
                replaceValues(value.getAsJsonObject(), replacements);
            } else if (value.isJsonArray()) { // Recursively call for arrays
                 // If the replacement for this key is a collection, replace the whole array
                if (replacements.containsKey(key) && replacements.get(key) instanceof Collection) {
                     JsonArray newArray = new Gson().toJsonTree(replacements.get(key)).getAsJsonArray();
                     jsonObject.add(key, newArray);
                } else { // Else, iterate through array elements
                    replaceValuesInArray(value.getAsJsonArray(), replacements, key);
                }
            }
            // Primitives not directly in replacements map and not part of a matched key are left unchanged.
        }
    }

    /**
     * Recursively replaces values within a JsonArray.
     * If an element in the array is a JsonObject, it calls {@link #replaceValues(JsonObject, Map)}.
     * If an element is another JsonArray, it calls itself.
     * This method is primarily for deep replacement within complex nested structures.
     *
     * @param jsonArray      The {@link JsonArray} to modify.
     * @param replacements   A map of replacements.
     * @param arrayKeyHint   The key name of this array in its parent object. (Currently unused in this specific logic but can be useful for context).
     */
    private static void replaceValuesInArray(JsonArray jsonArray, Map<String, Object> replacements, String arrayKeyHint) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement element = jsonArray.get(i);
            if (element.isJsonObject()) {
                replaceValues(element.getAsJsonObject(), replacements);
            } else if (element.isJsonArray()) {
                replaceValuesInArray(element.getAsJsonArray(), replacements, arrayKeyHint);
            }
            // Primitives within arrays are not replaced by this function unless the entire array
            // is replaced by the caller (replaceValues) if arrayKeyHint matches a key in replacements.
        }
    }

    /**
     * Updates values in a JSON structure (read from inputFilePath) with values from the data map.
     * Writes the modified JSON to outputFilePath. This method is similar to generatePayload but
     * might have a slightly different replacement strategy (intended by its name "update" vs "generate").
     * The current {@code replaceValue} helper seems to have a specific way of updating.
     *
     * @param inputFilePath  Path to the JSON file to be updated.
     * @param outputFilePath Path to write the updated JSON file.
     * @param data           A map containing keys to be updated and their new values.
     */
    public static void updatePayload(String inputFilePath, String outputFilePath, Map<String, Object> data) {
        try (FileReader fileReader = new FileReader(inputFilePath)) {
            JsonObject baseJsonData = JsonParser.parseReader(fileReader).getAsJsonObject();
            replaceValue(baseJsonData, data); // Uses a different helper method for replacement
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(baseJsonData, fileWriter);
            } catch (IOException e) {
                System.err.println("Error writing updated payload to " + outputFilePath + ": " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input payload for update not found: " + inputFilePath + " - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading input payload for update " + inputFilePath + ": " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON syntax in payload for update " + inputFilePath + ": " + e.getMessage());
        }
    }

    /**
     * Helper method to replace values in a JSON element (JsonObject or JsonArray).
     * This method differs from {@code replaceValues} in how it traverses and updates.
     * It directly replaces a value if a key in the {@code data} map matches a key in the JsonObject.
     * For JsonArrays, it checks for keys like "arrayName[index]" in the data map (not fully implemented here).
     *
     * @param element The JsonElement to modify.
     * @param data    Map containing data to update. Keys should match keys in the JSON.
     */
    private static void replaceValue(JsonElement element, Map<String, Object> data) {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (data.containsKey(key)) { // If data map has this key, replace the value
                    jsonObject.add(key, new Gson().toJsonTree(data.get(key)));
                } else { // Else, recurse if it is an object or array
                    replaceValue(value, data);
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement arrayElement = jsonArray.get(i);
                // For arrays, it only recurses if elements are objects/arrays.
                // Direct update of array elements by index (e.g. data key "array[0]") is not explicitly handled here.
                if (arrayElement.isJsonObject() || arrayElement.isJsonArray()) {
                    replaceValue(arrayElement, data);
                }
                // Example for indexed replacement (if needed):
                // String arrayIndexedKey = String.format("%s[%d]", "array_key_name", i);
                // if (data.containsKey(arrayIndexedKey)) {
                //     jsonArray.set(i, new Gson().toJsonTree(data.get(arrayIndexedKey)));
                // }
            }
        }
    }

    /**
     * Modifies a specific "Appointment" JSON structure.
     * Updates fields like start time, end time, participant references, and appointment type coding.
     *
     * @param input          Path to the input Appointment JSON file.
     * @param output         Path to write the modified Appointment JSON file.
     * @param start          New start time string.
     * @param end            New end time string.
     * @param patient        New patient ID (without "Patient/" prefix).
     * @param practitioner   New practitioner ID (without "Practitioner/" prefix).
     * @param location       New location ID (without "Location/" prefix).
     * @param id             New ID for the appointmentType coding.
     * @param code           New code for the appointmentType coding.
     * @throws Exception if there is an error reading/writing files or processing JSON.
     */
    public static void modifyAppointmentJson(String input, String output, String start, String end, String patient, String practitioner, String location, String id, String code) throws Exception {
        File inputFile = new File(input);
        File outputFile = new File(output);
        outputFile.getParentFile().mkdirs(); // Ensure output directory exists
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputFile);

        ((ObjectNode) rootNode).put("start", start);
        ((ObjectNode) rootNode).put("end", end);

        JsonNode participantArray = rootNode.path("participant"); // Use path to avoid NPE if "participant" is missing
        if (participantArray.isArray()) {
            for (JsonNode participant : participantArray) {
                JsonNode actorNode = participant.path("actor");
                String reference = actorNode.path("reference").asText(""); // Default to empty string if missing
                if (reference.startsWith("Patient/")) {
                    ((ObjectNode) actorNode).put("reference", "Patient/" + patient);
                } else if (reference.startsWith("Practitioner/")) {
                    ((ObjectNode) actorNode).put("reference", "Practitioner/" + practitioner);
                } else if (reference.startsWith("Location/")) {
                    ((ObjectNode) actorNode).put("reference", "Location/" + location);
                }
            }
        }

        // Assuming appointmentType.coding is an object, not an array. If it can be an array, logic needs adjustment.
        JsonNode codingNode = rootNode.path("appointmentType").path("coding");
        if (!codingNode.isMissingNode() && codingNode.isObject()) { // Check if node exists and is an object
            ((ObjectNode) codingNode).put("id", id);
            ((ObjectNode) codingNode).put("code", code);
        } else if (codingNode.isArray() && codingNode.size() > 0) { // Handle if it is an array (take first element)
             ((ObjectNode) codingNode.get(0)).put("id", id);
             ((ObjectNode) codingNode.get(0)).put("code", code);
        }


        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, rootNode);
    }

    /**
     * Creates a "PaymentReconciliation" JSON payload.
     * Sets "created" and "paymentDate" to the current date-time.
     * Updates the patient reference in the "extension" array.
     *
     * @param input   Path to the input PaymentReconciliation JSON template.
     * @param output  Path to write the generated PaymentReconciliation JSON file.
     * @param patId   The patient ID to set in the payload.
     * @throws RuntimeException if there is an error reading/writing files or processing JSON.
     */
    public static void createPaymentReconciliationPayload(String input, String output, String patId) {
        File inputFile = new File(input);
        File outputFile = new File(output);
        outputFile.getParentFile().mkdirs(); // Ensure output directory exists
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root;
        try {
            root = (ObjectNode) objectMapper.readTree(inputFile);
        } catch (IOException e) {
            throw new RuntimeException("Error reading PaymentReconciliation template: " + input, e);
        }

        String now = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        root.put("created", now);
        root.put("paymentDate", now); // Assuming paymentDate should also be now

        String patientValue = "Patient/" + patId;
        JsonNode extensions = root.path("extension"); // Use path to avoid NPE
        if (extensions.isArray()) {
            extensions.forEach(node -> {
                if (node.has("url") && "PaymentReconciliation#patient".equals(node.get("url").asText())) {
                    ObjectNode valueReference = (ObjectNode) node.path("valueReference"); // Use path
                    if (!valueReference.isMissingNode()) {
                        valueReference.put("reference", patientValue);
                        valueReference.put("display", patientValue); // Often display is same as reference or a name
                    }
                }
            });
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);
        } catch (IOException e) {
            throw new RuntimeException("Error writing PaymentReconciliation payload: " + output, e);
        }
    }
}
