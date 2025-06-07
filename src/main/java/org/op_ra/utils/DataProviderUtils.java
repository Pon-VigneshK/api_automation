package org.op_ra.utils;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.*;

import static org.op_ra.utils.JsonUtils.generateTestDataJson;
import static org.op_ra.utils.JsonUtils.getTestDataDetails;

/**
 * Provides TestNG data provider methods for supplying test data to test methods.
 * This class reads test data from JSON files, which can be generated from various sources
 * like databases or Excel sheets using {@link JsonUtils}.
 *
 * Test methods can use the {@code dataProvider = "getJsonData", dataProviderClass = DataProviderUtils.class}
 * attribute to receive test data.
 */
@SuppressWarnings("unchecked") // Suppressing warnings for generic type casting
public final class DataProviderUtils {

    // Stores the test data read from JSON to avoid reading the file multiple times.
    private static List<Map<String, Object>> testDataFromJson = new ArrayList<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DataProviderUtils() {
        // Private constructor
    }

    /**
     * TestNG DataProvider method to supply test data to test methods based on their names.
     * It reads test data from a JSON file (generated if not present or empty).
     * Filters the data based on the test case name (method name) and an "execute" flag in the data.
     *
     * <p>The JSON test data is expected to be an array of objects, where each object represents
     * a set of data for a test iteration and contains at least "testcasename" and "execute" keys.</p>
     *
     * <p>Example JSON structure for test data:</p>
     * <pre>{@code
     * {
     *   "DEV": { // Environment name
     *     "test_case_group_1": [
     *       {
     *         "testcasename": "testMethodName1",
     *         "execute": "yes",
     *         "param1": "value1",
     *         "param2": 123
     *       },
     *       {
     *         "testcasename": "testMethodName1",
     *         "execute": "yes",
     *         "param1": "value2",
     *         "param2": 456
     *       },
     *       {
     *         "testcasename": "testMethodName2",
     *         "execute": "no",
     *         "data": "sample"
     *       }
     *     ]
     *   }
     * }
     * }</pre>
     *
     * @param method The test method for which data is being provided.
     *               The name of this method is used to filter relevant test data.
     * @return An array of objects, where each object is a {@code Map<String, Object>}
     *         representing one iteration of test data for the test method.
     *         Returns an empty array if no matching data is found or if data is marked not to execute.
     * @see JsonUtils#generateTestDataJson()
     * @see JsonUtils#getTestDataDetails()
     */
    @DataProvider(name = "getJsonData") // Name the DataProvider for easy reference in tests
    public static Object[] getJsonData(Method method) {
        String testCaseName = method.getName();
        List<Map<String, Object>> iterationList = new ArrayList<>();

        // Initialize test data from JSON if it is not already loaded
        if (testDataFromJson.isEmpty()) {
            JsonUtils.generateTestDataJson(); // Ensure JSON data is generated from DB/Excel if configured
            testDataFromJson = JsonUtils.getTestDataDetails(); // Load data from the central JSON file
        }

        // Filter data for the current test method and where execute flag is "yes"
        for (Map<String, Object> testDataInstance : testDataFromJson) {
            if (String.valueOf(testDataInstance.get("testcasename")).equalsIgnoreCase(testCaseName) &&
                String.valueOf(testDataInstance.get("execute")).equalsIgnoreCase("yes")) {
                iterationList.add(testDataInstance);
            }
        }

        // Remove duplicates if any (though typically each map should be unique per iteration)
        // Set<Map<String, Object>> set = new HashSet<>(iterationList);
        // iterationList.clear();
        // iterationList.addAll(set);
        // Note: Using HashSet might reorder data if order is important.
        // If duplicates are not expected or order is critical, this step can be skipped.

        return iterationList.toArray();
    }
}
