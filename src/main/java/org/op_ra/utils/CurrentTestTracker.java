package org.op_ra.utils;

/**
 * {@code CurrentTestTracker} is a utility class that uses a {@link ThreadLocal} variable
 * to keep track of the name of the currently executing test. This is particularly useful
 * in a multithreaded environment to identify the test that is running on a specific thread.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to set, get, and clear the name of the current test.</p>
 *
 * <pre>{@code
 * // Setting the name of the current test
 * CurrentTestTracker.set("myTestMethod");
 *
 * // Getting the name of the current test
 * String currentTest = CurrentTestTracker.get();
 *
 * // Clearing the name of the current test
 * CurrentTestTracker.clear();
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class CurrentTestTracker {
    private static final ThreadLocal<String> CURRENT_TEST = new ThreadLocal<>();

    private CurrentTestTracker() {
    }

    /**
     * Returns the name of the currently executing test for the current thread.
     *
     * @return The name of the current test.
     */
    public static String get() {
        return CURRENT_TEST.get();
    }

    /**
     * Sets the name of the currently executing test for the current thread.
     *
     * @param name The name of the test to set.
     */
    public static void set(String name) {
        CURRENT_TEST.set(name);
    }

    /**
     * Removes the name of the currently executing test for the current thread.
     * This is useful for cleaning up after a test has finished.
     */
    public static void clear() {
        CURRENT_TEST.remove();
    }
}
