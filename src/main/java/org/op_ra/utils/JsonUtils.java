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
     * Read RunnerList data from Excel and write it to a JSON file.
     *
     * @param sheetName Name of the Excel sheet containing data.
     */
    public static void generateRunnerListJsonDataFromExcel(String sheetName) {
        try (FileInputStream fs = new FileInputStream(FrameworkConstants.getExcelFilePath())) {
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            int lastrownum = sheet.getLastRowNum();
            int lastcolnum = sheet.getRow(0).getLastCellNum();
            HashMap<String, Object> map;
            ArrayList<HashMap<String, Object>> testDataList = new ArrayList<>();
            String testCaseListName = "testCaseLists";
            for (int i = 1; i <= lastrownum; i++) {
                map = new HashMap<>();
                for (int j = 0; j < lastcolnum; j++) {
                    String key = sheet.getRow(0).getCell(j).getStringCellValue();
                    String value = convertCellValueToString(sheet.getRow(i).getCell(j));
                    map.put(key, value);
                }
                testDataList.add(map);
                finalTestList.put(testCaseListName, testDataList); // Use a unique key for each test data entry
            }
            testRunnerHashMap.put(FrameworkConstants.getRunmanager(), finalTestList);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FrameworkConstants.getTestCaseJsonPath()), testRunnerHashMap);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert cell value to a string based on its type.
     *
     * @param cell The Excel cell to convert.
     * @return String representation of the cell value.
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
                if (numericValue % 1 == 0) {
                    return String.valueOf((int) numericValue);
                } else {
                    return String.valueOf(numericValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
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

    public static void generatePayload(String inputFilePath, String outputFilePath, Map<String, Object> data) {
        try (FileReader fileReader = new FileReader(inputFilePath)) {
            JsonObject baseJsonData = JsonParser.parseReader(fileReader).getAsJsonObject();
            replaceValues(baseJsonData, data);
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(baseJsonData, fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generatePayloadBDD(String inputFilePath, String outputFilePath, Map<String, Object> data) {
        try (FileReader fileReader = new FileReader(inputFilePath)) {
            JsonObject baseJsonData = JsonParser.parseReader(fileReader).getAsJsonObject();
            replaceValues(baseJsonData, data);
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(baseJsonData, fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
            return new String(Files.readAllBytes(Paths.get(outputFilePath)));

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }


    private static void replaceValues(JsonObject jsonObject, Map<String, Object> replacements) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonObject()) {
                replaceValues(value.getAsJsonObject(), replacements);
            } else if (value.isJsonArray()) {
                replaceValuesInArray(value.getAsJsonArray(), replacements, key);
            } else if (replacements.containsKey(key)) {
                jsonObject.add(key, JsonParser.parseString(new Gson().toJson(replacements.get(key))));
            }
        }
    }

    private static void replaceValuesInArray(JsonArray jsonArray, Map<String, Object> replacements, String keyToReplace) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement element = jsonArray.get(i);
            if (element.isJsonObject()) {
                replaceValues(element.getAsJsonObject(), replacements);
            } else if (element.isJsonArray()) {
                replaceValuesInArray(element.getAsJsonArray(), replacements, keyToReplace);
            } else if (element.isJsonPrimitive() && replacements.containsKey(keyToReplace)) {
                jsonArray.set(i, JsonParser.parseString(new Gson().toJson(replacements.get(keyToReplace))));
            }
        }
    }

    public static void updatePayload(String inputFilePath, String outputFilePath, Map<String, Object> data) {
        try (FileReader fileReader = new FileReader(inputFilePath)) {
            JsonObject baseJsonData = JsonParser.parseReader(fileReader).getAsJsonObject();
            replaceValue(baseJsonData, data);
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(baseJsonData, fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void replaceValue(JsonElement element, Map<String, Object> data) {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if (data.containsKey(key)) {
                    jsonObject.add(key, new Gson().toJsonTree(data.get(key)));
                } else {
                    replaceValue(value, data);
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement arrayElement = jsonArray.get(i);
                if (arrayElement.isJsonObject()) {
                    replaceValue(arrayElement, data);
                } else if (arrayElement.isJsonPrimitive()) {
                    // Assuming keys for array elements are provided with a format like "arrayName[index]"
                    String key = String.format("array[%d]", i);
                    if (data.containsKey(key)) {
                        jsonArray.set(i, new Gson().toJsonTree(data.get(key)));
                    }
                }
            }
        }
    }

    public static void modifyAppointmentJson(String input, String output, String start, String end, String patient, String practitioner, String location, String id, String code) throws Exception {
        File inputFile = new File(input);
        File outputFile = new File(output);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputFile);
        ((ObjectNode) rootNode).put("start", start);
        ((ObjectNode) rootNode).put("end", end);
        JsonNode participantArray = rootNode.get("participant");
        if (participantArray.isArray()) {
            for (JsonNode participant : participantArray) {
                String reference = participant.get("actor").get("reference").asText();
                if (reference.startsWith("Patient/")) {
                    ((ObjectNode) participant.get("actor")).put("reference", "Patient/" + patient);
                } else if (reference.startsWith("Practitioner/")) {
                    ((ObjectNode) participant.get("actor")).put("reference", "Practitioner/" + practitioner);
                } else if (reference.startsWith("Location/")) {
                    ((ObjectNode) participant.get("actor")).put("reference", "Location/" + location);
                }
            }
        }
        JsonNode appointmentTypeNode = rootNode.get("appointmentType").get("coding");
        ((ObjectNode) appointmentTypeNode).put("id", id);
        ((ObjectNode) appointmentTypeNode).put("code", code);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, rootNode);
    }

    public static void createPaymentReconciliationPayload(String input, String output, String patId) {
        File inputFile = new File(input);
        File outputFile = new File(output);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = null;
        try {
            root = (ObjectNode) objectMapper.readTree(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String now = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        root.put("created", now);
        root.put("paymentDate", now);
        String patientValue = "Patient/" + patId;
        root.withArray("extension").forEach(node -> {
            if (node.has("url") && "PaymentReconciliation#patient".equals(node.get("url").asText())) {
                ObjectNode valueReference = (ObjectNode) node.get("valueReference");
                valueReference.put("reference", patientValue);
                valueReference.put("display", patientValue);
            }
        });
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

