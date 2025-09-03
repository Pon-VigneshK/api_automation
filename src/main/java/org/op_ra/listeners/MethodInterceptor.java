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
 * {@code MethodInterceptor} is a TestNG listener that implements the {@link IMethodInterceptor}
 * interface. This listener allows for the modification of the list of test methods that TestNG
 * is about to run. It provides a way to filter or reorder tests dynamically before they are executed.
 *
 * <p>In this framework, this listener is used to select which tests to run based on a JSON
 * configuration file. It reads a list of test cases and their execution status ("yes" or "no")
 * from a JSON file and only includes the tests that are marked for execution.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To use this listener, it must be specified in the TestNG suite XML file.</p>
 *
 * <pre>{@code
 * <suite name="MyTestSuite">
 *     <listeners>
 *         <listener class-name="org.op_ra.listeners.MethodInterceptor"/>
 *     </listeners>
 *     <test name="MyTest">
 *         ...
 *     </test>
 * </suite>
 * }</pre>
 *
 * @see IMethodInterceptor
 * @see JsonUtils
 * @version 1.0
 * @since 2023-10-27
 */
public class MethodInterceptor implements IMethodInterceptor {

    /**
     * This method is called by TestNG and allows for the modification of the list of test methods.
     * It reads the test execution details from a JSON file and filters the methods based on whether
     * the "execute" flag is set to "yes". It also sets the priority and invocation count for the
     * selected test methods.
     *
     * @param methods The list of test methods that TestNG is about to run.
     * @param iTestContext The context of the test.
     * @return A list of test methods that should be run.
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext iTestContext) {
        List<IMethodInstance> results = new ArrayList<>();
        JsonUtils.generateRunnerListJsonData();
        List<Map<String, Object>> list = JsonUtils.getTestDetails(FrameworkConstants.getRunmanager(), FrameworkConstants.getTestcaselist());
        for (int i = 0; i < methods.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (methods.get(i).getMethod().getMethodName().equalsIgnoreCase(String.valueOf(list.get(j).get("testcasename"))) &&
                        String.valueOf(list.get(j).get("execute")).equalsIgnoreCase("yes")) {
                    methods.get(i).getMethod().setPriority(Integer.parseInt(String.valueOf(list.get(j).get("priority"))));
                    methods.get(i).getMethod().setInvocationCount(Integer.parseInt((String) list.get(j).get("count")));
                    results.add(methods.get(i));
                }
            }
        }
        return results;
    }
}
