package org.op_ra.listeners;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.utils.PropertyUtils;
import org.op_ra.utils.TestRunContext;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * {@code RetryFailedTests} is a TestNG listener that implements the {@link IRetryAnalyzer}
 * interface. This listener provides a mechanism to automatically retry failed test methods.
 *
 * <p>The decision to retry a test is based on the "retry" property in the configuration file.
 * If the property is set to "yes", this listener will retry a failed test up to a maximum
 * number of retries (currently set to 1).</p>
 *
 * <p><b>Usage:</b></p>
 * <p>This retry analyzer is set for all test methods at runtime by the {@link AnnotationTransformer}.</p>
 *
 * @see IRetryAnalyzer
 * @see AnnotationTransformer
 * @version 1.0
 * @since 2023-10-27
 */
public class RetryFailedTests implements IRetryAnalyzer {
    private final int retries = 1;
    private int count = 0;

    /**
     * This method is called by TestNG when a test method fails. It determines whether the
     * failed test should be retried.
     *
     * <p>A retry will be attempted if the "retry" property in the configuration file is set
     * to "yes" and the number of retries has not exceeded the maximum limit.</p>
     *
     * @param result The result of the failed test method.
     * @return {@code true} if the test should be retried, {@code false} otherwise.
     */
    @Override
    public boolean retry(ITestResult result) {
        boolean state = false;
        if (PropertyUtils.getValue(ConfigProperties.RETRY).equalsIgnoreCase("yes")) {
            state = count < retries;
            if (state) {
                TestRunContext.incrementIteration();
            }
            count++;
            return state;
        } else {
            return state;
        }
    }
}
