package org.op_ra.listeners;

import org.op_ra.constants.FrameworkConstants;
import org.op_ra.utils.JsonUtils;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implements {@link org.testng.IMethodInterceptor} to leverage the abstract methods.
 * Mostly used to read the data from JSON or Excel and decide which tests need to run.
 *
 * <pre>Please make sure to add the listener details in the testng.xml file.</pre>
 */

public class MethodInterceptor implements IMethodInterceptor {
    /**
     * Intercepts the existing test methods and changes the annotation values at runtime.
     * Values are fetched from the JSON.
     * The user has to choose yes/no in the Excel executed column.
     * For example, if there are 3 tests named a, b, c that need to be run, it reads the runner list JSON and understands the user wants to
     * run only tests a and c. It then returns the updated list after performing the comparisons.
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext iTestContext) {
        List<IMethodInstance> results = new ArrayList<>();
        // generate runner list json
        JsonUtils.generateRunnerListJsonData();
        List<Map<String, Object>> list = JsonUtils.getTestDetails(FrameworkConstants.getRunmanager(), FrameworkConstants.getTestcaselist());
        for (int i = 0; i < methods.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (methods.get(i).getMethod().getMethodName().equalsIgnoreCase(String.valueOf(list.get(j).get("testcasename"))) &&
                        String.valueOf(list.get(j).get("execute")).equalsIgnoreCase("yes")) {
                    //methods.get(i).getMethod().setDescription(String.valueOf(list.get(j).get("testdescription")));
                    methods.get(i).getMethod().setPriority(Integer.parseInt(String.valueOf(list.get(j).get("priority"))));
                    methods.get(i).getMethod().setInvocationCount(Integer.parseInt((String) list.get(j).get("count")));
//                    methods.get(i).getMethod().setPriority(Integer.parseInt(String.valueOf(list.get(j).get("priority"))));
//                    methods.get(i).getMethod().setInvocationCount((Integer) list.get(j).get("count"));

                    results.add(methods.get(i));
                }
            }
        }
        return results;
    }
//    @Override
//    public List<IMethodInstance> intercept(List<IMethodInstance> method, ITestContext iTestContext) {
//        List<IMethodInstance> results = new ArrayList<>();
//        JsonUtils.generateRunnerListJsonDataFromExcel(FrameworkConstants.getRunmangerExcelSheet());
//        List<Map<String, Object>> list = JsonUtils.getTestDetails(FrameworkConstants.getRunmanager(), FrameworkConstants.getTestcaselist());
//        for (int i = 0; i < method.size(); i++) {
//            for (int j = 0; j < list.size(); j++) {
//                if (method.get(i).getMethod().getMethodName().equalsIgnoreCase(String.valueOf(list.get(j).get("testcasename"))) &&
//                        String.valueOf(list.get(j).get("execute")).equalsIgnoreCase("yes")) {
//                    method.get(i).getMethod().setDescription(String.valueOf(list.get(j).get("testdescription")));
//                    method.get(i).getMethod().setInvocationCount(Integer.parseInt((String) list.get(j).get("count")));
//                    method.get(i).getMethod().setPriority(Integer.parseInt(String.valueOf(list.get(j).get("priority"))));
//                    results.add(method.get(i));
//                }
//            }
//        }
//        return results;
//    }
}
