package org.op_ra.reports;

import com.aventstack.extentreports.ExtentTest;

/**
 * Manages {@link ExtentTest} instances for thread-safe reporting in ExtentReports.
 * This class uses a {@link ThreadLocal} variable to store the {@link ExtentTest} object,
 * ensuring that each thread has its own instance of the test report. This is crucial
 * when running tests in parallel to prevent logs from different tests being mixed up.
 * <p>
 * Methods are provided to set, get, and unload the {@link ExtentTest} instance for the current thread.
 * </p>
 * @see ExtentReport
 * @see ExtentLogger
 */
public final class ExtentManager {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ExtentManager() {
        // Private constructor
    }

    // ThreadLocal variable to store ExtentTest instance per thread
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    /**
     * Returns the {@link ExtentTest} instance for the current thread.
     * This allows logging methods (e.g., in {@link ExtentLogger}) to access the correct
     * test report object associated with the currently executing test.
     *
     * @return The {@link ExtentTest} instance for the current thread.
     *         Returns {@code null} if no test has been set for the current thread.
     */
    static ExtentTest getExtentTest() { // Package-private access, used by ExtentReport and ExtentLogger
        return extentTestThreadLocal.get();
    }

    /**
     * Sets the {@link ExtentTest} instance for the current thread.
     * This is typically called when a new test starts (e.g., in a TestNG listener like {@link org.op_ra.listeners.ListenerClass#onTestStart}).
     *
     * @param test The {@link ExtentTest} object to be associated with the current thread.
     *             This object is usually created by {@link com.aventstack.extentreports.ExtentReports#createTest(String)}.
     */
    static void setExtentTest(ExtentTest test) { // Package-private access
        extentTestThreadLocal.set(test);
    }

    /**
     * Removes the {@link ExtentTest} instance from the current thread'''s ThreadLocal storage.
     * This should be called after a test has finished (e.g., in a TestNG listener like {@link org.op_ra.listeners.ListenerClass#onFinish(org.testng.ISuite)})
     * to prevent memory leaks and ensure a clean state for subsequent tests on the same thread
     * (if threads are reused by the test runner).
     */
    static void unloadExtentTest() { // Package-private access
        extentTestThreadLocal.remove();
    }
}
