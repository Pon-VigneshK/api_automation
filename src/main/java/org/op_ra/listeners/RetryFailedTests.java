package org.op_ra.listeners;

import org.op_ra.enums.ConfigProperties;
import org.op_ra.utils.PropertyUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTests implements IRetryAnalyzer {
    private final int retries = 1;
    /**
     * Implements {@link IRetryAnalyzer}.<p>
     * Assists in rerunning the failed tests.<p>
     */
    private int count = 0;

    /**
     * Returns true when a retry is needed and false otherwise.
     * Maximum retries allowed is one time.
     * Retry will occur if the user desires and has set the value in the property file.
     */

    @Override
    public boolean retry(ITestResult result) {
        boolean state = false;
        if (PropertyUtils.getValue(ConfigProperties.RETRY).equalsIgnoreCase("yes")) {
            state = count < retries;
            count++;
            return state;
        } else {
            return state;
        }
    }
}
