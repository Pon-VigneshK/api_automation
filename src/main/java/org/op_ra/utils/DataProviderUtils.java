package org.op_ra.utils;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.*;

import static org.op_ra.utils.JsonUtils.generateTestDataJson;
import static org.op_ra.utils.JsonUtils.getTestDataDetails;
@Deprecated
public final class DataProviderUtils {
    private static List<Map<String, Object>> testDataFromJson = new ArrayList();

    private DataProviderUtils() {
    }

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

//    @DataProvider(name = "getData")
//    public static Object[] getData(Method method) {
//        String testcasename = method.getName();
//        List<Map<String, String>> list = ExcelUtils.getTestDetails(FrameworkConstants.getDataExcelSheet());
//        List<Map<String, String>> iterationlist = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).get("testcasename").equalsIgnoreCase(testcasename)) {
//                if (list.get(i).get("execute").equalsIgnoreCase("yes")) {
//                    iterationlist.add(list.get(i));
//                }
//            }
//        }
//        return iterationlist.toArray();
//    }

}
