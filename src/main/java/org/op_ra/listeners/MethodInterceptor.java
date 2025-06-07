package org.op_ra.listeners;

import org.op_ra.utils.JsonUtils; // Assuming JsonUtils for test data/runner lists
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implements {@link org.testng.IMethodInterceptor} to dynamically control which test methods are run.
 * This interceptor reads a list of test cases to be executed from an external source (e.g., a JSON file
 * generated from an Excel sheet or database via {@link JsonUtils}).
 * It then filters the provided list of TestNG methods, allowing only those specified in the
 * external list to be executed.
 * <p>
 * This is useful for creating dynamic test suites or for running a subset of tests based on
 * external configuration without modifying TestNG XML files directly.
 * </p>
 * To use this interceptor, it needs to be specified in the TestNG suite XML file:
 * <pre>{@code
 * <suite name="MySuite">
 *     <listeners>
 *         <listener class-name="org.op_ra.listeners.MethodInterceptor"/>
 *     </listeners>
 *     ...
 * </suite>
 * }</pre>
 */
public class MethodInterceptor implements IMethodInterceptor {

    /**
     * Intercepts the list of test methods that TestNG intends to run and returns a modified list.
     * This implementation filters test methods based on an execution list fetched from
     * {@link JsonUtils#getTestDetails(String, String)}.
     *
     * @param methods A list of {@link org.testng.IMethodInstance} objects representing the test methods
     *                TestNG plans to run.
     * @param context The {@link org.testng.ITestContext} for the current test run.
     * @return A list of {@link org.testng.IMethodInstance} objects that should actually be run.
     *         This list will be a subset of the input {@code methods} list.
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        List<IMethodInstance> result = new ArrayList<>();
        // Assuming getRunmanager() and "testCaseLists" are the correct keys for your runner list JSON.
        // These might need to be fetched from FrameworkConstants or context.
        String runManager = context.getCurrentXmlTest().getSuite().getParameter("runmanager"); // Or from FrameworkConstants
        if (runManager == null || runManager.isEmpty()){
             // Attempt to get from suite name if parameter not set
            runManager = context.getSuite().getName();
        }

        // Fallback if still null, or use a default from FrameworkConstants
        if (runManager == null || runManager.isEmpty()) {
            runManager = org.op_ra.constants.FrameworkConstants.getRunmanager();
        }


        List<Map<String, Object>> list = JsonUtils.getTestDetails(runManager, "testCaseLists");
        // If JsonUtils.getTestDetails expects FrameworkConstants.getRunmanager(), ensure it'''s set appropriately.

        if (list.isEmpty()) {
            System.out.println("WARNING: Test execution list from JSON is empty for run manager: " + runManager + ". Running all discovered tests.");
            // If the list is empty, you might choose to run all methods or none.
            // Running all is TestNG'''s default behavior if interceptor returns original list or empty.
            // To run none, return an empty list: return new ArrayList<>();
            // To run all, return methods: return methods;
            // For safety, if the JSON list is empty, it might be better to run no tests or log a severe error.
            // However, current implementation will add all methods if the condition below is not met.
            // This behavior has been changed to run only specified tests. If list is empty, result will be empty.
        }

        for (IMethodInstance methodInstance : methods) {
            String methodName = methodInstance.getMethod().getMethodName();
            boolean found = false;
            for (Map<String, Object> testDetails : list) {
                String testCaseName = String.valueOf(testDetails.get("testcasename"));
                String executeFlag = String.valueOf(testDetails.get("execute"));

                if (methodName.equalsIgnoreCase(testCaseName) && executeFlag.equalsIgnoreCase("yes")) {
                    // Additional check: ensure the method belongs to the correct class if class names are also in JSON
                    // String classNameFromJson = String.valueOf(testDetails.get("classname"));
                    // String currentMethodClassName = methodInstance.getMethod().getTestClass().getName();
                    // if (classNameFromJson != null && currentMethodClassName.endsWith(classNameFromJson)) {
                    //    result.add(methodInstance);
                    //    found = true;
                    //    break;
                    // } else if (classNameFromJson == null) { // if classname is not in JSON, just match method name
                         result.add(methodInstance);
                         found = true;
                         break;
                    // }
                }
            }
            if (found) {
                System.out.println("MethodInterceptor: Adding method to run: " + methodName);
            } else {
                 System.out.println("MethodInterceptor: Skipping method (not in execution list or not marked 'yes'): " + methodName);
            }
        }

        if (result.isEmpty() && !methods.isEmpty() && !list.isEmpty()){
            System.out.println("WARNING: MethodInterceptor resulted in an empty list of tests to run, " +
                               "even though TestNG discovered methods and an execution list was provided. " +
                               "Check testcasename matches and execute flags in your runner JSON for run manager: " + runManager);
        } else if (result.isEmpty() && !methods.isEmpty() && list.isEmpty()){
             System.out.println("WARNING: MethodInterceptor: No tests will run as the execution list from JSON is empty for run manager: " + runManager);
        }

        return result;
    }
}
