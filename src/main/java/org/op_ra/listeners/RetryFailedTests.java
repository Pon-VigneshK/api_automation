package org.op_ra.listeners;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.utils.PropertyUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Implements {@link org.testng.IRetryAnalyzer} to provide a mechanism for retrying failed TestNG tests.
 * The number of retry attempts is configurable through the {@code config.properties} file
 * using the key associated with {@link ConfigProperties#RETRY_FAILED_TESTS}.
 * If the property is set to "yes" (case-insensitive), tests will be retried.
 * The maximum number of retries is hardcoded (e.g., 1, meaning one retry after initial failure).
 * <p>
 * To use this retry analyzer, it can be set in a listener like {@link AnnotationTransformer}
 * or directly in the {@code @Test} annotation:
 * <pre>
 * {@code @Test(retryAnalyzer = RetryFailedTests.class)}
 * </pre>
 * </p>
 */
public class RetryFailedTests implements IRetryAnalyzer {

    private int count = 0;
    // Max retries. For example, 1 means one retry (total 2 attempts).
    // This could also be made configurable.
    private static final int MAX_RETRIES = 1;

    /**
     * Determines whether a failed test method should be retried.
     * Retries the test if the "RETRY_FAILED_TESTS" property is set to "yes"
     * and the current retry count is less than {@link #MAX_RETRIES}.
     *
     * @param result The result of the test method that just failed.
     * @return {@code true} if the test method should be retried, {@code false} otherwise.
     */
    @Override
    public boolean retry(ITestResult result) {
        boolean shouldRetry = false;
        String retryFlag = null;
        try {
            retryFlag = PropertyUtils.getValue(ConfigProperties.RETRY_FAILED_TESTS);
        } catch (Exception e) {
            // Log that property is not found, and default to not retrying.
            System.err.println("Property for RETRY_FAILED_TESTS not found or error reading properties. Defaulting to no retry. Error: " + e.getMessage());
            retryFlag = "no"; // Default to no if property is missing or causes error
        }

        if (retryFlag != null && retryFlag.equalsIgnoreCase("yes")) {
            if (count < MAX_RETRIES) {
                count++;
                System.out.println("Retrying test: " + result.getMethod().getMethodName() + " for the " + count + " time.");
                shouldRetry = true;
            } else {
                System.out.println("Max retries reached for test: " + result.getMethod().getMethodName() + ". No more retries.");
            }
        } else {
            System.out.println("Retry for failed tests is disabled or property not set to 'yes'. No retry for: " + result.getMethod().getMethodName());
        }
        return shouldRetry;
    }
}
