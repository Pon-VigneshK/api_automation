package org.op_ra.reports;

import com.aventstack.extentreports.ExtentTest;

/**
 * {@code ExtentManager} is a final class that provides thread-safe management of the
 * {@link ExtentTest} instance. This class uses a {@link ThreadLocal} variable to store
 * the {@code ExtentTest} object, ensuring that each thread has its own instance of the
 * report test. This is crucial for running tests in parallel without conflicts in reporting.
 *
 * <p>This class is designed to be used by other classes in the reporting package and is not
 * intended to be instantiated directly.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class are called by {@link ExtentReport} and {@link ExtentLogger}
 * to manage the {@code ExtentTest} instance for the current thread.</p>
 *
 * <pre>{@code
 * // Setting the ExtentTest instance for the current thread
 * ExtentManager.setExtentTest(extent.createTest("MyTest"));
 *
 * // Getting the ExtentTest instance for the current thread
 * ExtentTest test = ExtentManager.getExtentTest();
 *
 * // Unloading the ExtentTest instance for the current thread
 * ExtentManager.unloadExtentTest();
 * }</pre>
 *
 * @see ExtentTest
 * @see ThreadLocal
 * @version 1.0
 * @since 2023-10-27
 */
public final class ExtentManager {
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private ExtentManager() {
    }

    /**
     * Returns the {@link ExtentTest} instance for the current thread.
     *
     * @return The {@link ExtentTest} instance for the current thread.
     */
    static ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }

    /**
     * Sets the {@link ExtentTest} instance for the current thread.
     *
     * @param test The {@link ExtentTest} instance to be set for the current thread.
     */
    static void setExtentTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    /**
     * Removes the {@link ExtentTest} instance for the current thread. This is important
     * to prevent memory leaks and to ensure that the next test in the same thread
     * starts with a clean slate.
     */
    static void unloadExtentTest() {
        extentTestThreadLocal.remove();
    }
}
