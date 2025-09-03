package org.op_ra.utils;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.op_ra.utils.JsonUtils.generateTestDataJson;
import static org.op_ra.utils.JsonUtils.getTestDataDetails;

/**
 * {@code DataProviderUtils} is a final class that provides a TestNG data provider for JSON data.
 * This class is responsible for reading test data from a JSON file and providing it to test methods.
 *
 * <p>This class is marked as {@link Deprecated} and may be removed in a future version.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To use the data provider, annotate a test method with {@code @Test(dataProvider = "getJsonData", dataProviderClass = DataProviderUtils.class)}.</p>
 *
 * <pre>{@code
 * @Test(dataProvider = "getJsonData", dataProviderClass = DataProviderUtils.class)
 * public void myTest(Map<String, Object> data) {
 *     // Test logic using the data from the JSON file
 * }
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 * @deprecated This class is deprecated and may be replaced by a different data provider implementation.
 */
@Deprecated
public final class DataProviderUtils {
    private static List<Map<String, Object>> testDataFromJson = new ArrayList();

    private DataProviderUtils() {
    }

    /**
     * A TestNG data provider that fetches test data from a JSON file. It filters the data based on the
     * test case name and an "execute" flag in the JSON file.
     *
     * @param method The test method that will be using the data provider.
     * @return An array of objects, where each object is a map representing a set of test data.
     */
    @DataProvider
    public static Object[] getJsonData(Method method) {
        String testCaseName = method.getName();
        List<Map<String, Object>> iterationList = new ArrayList();
        if (testDataFromJson.isEmpty()) {
            generateTestDataJson();
            testDataFromJson = getTestDataDetails();
        }
        for (int i = 0; i < testDataFromJson.size(); i++) {
            if (String.valueOf(testDataFromJson.get(i).get("testcasename")).equalsIgnoreCase(testCaseName) &&
                    String.valueOf(testDataFromJson.get(i).get("execute")).equalsIgnoreCase("yes")) {
                iterationList.add(testDataFromJson.get(i));
            }
        }
        Set<Map<String, Object>> set = new HashSet(iterationList);
        iterationList.clear();
        iterationList.addAll(set);
        return iterationList.toArray();
    }
}
