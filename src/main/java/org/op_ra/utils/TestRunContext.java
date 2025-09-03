package org.op_ra.utils;

/**
 * {@code TestRunContext} is a final class that tracks per-test transient context, such as the
 * iteration ID for retries, using a {@link ThreadLocal} variable. This ensures that each
 * thread has its own context, which is important for running tests in parallel.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to manage the test run context.</p>
 *
 * <pre>{@code
 * // Resetting the iteration ID
 * TestRunContext.reset();
 *
 * // Incrementing the iteration ID
 * TestRunContext.incrementIteration();
 *
 * // Getting the current iteration ID
 * int iterationId = TestRunContext.getIterationId();
 *
 * // Clearing the context
 * TestRunContext.clear();
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public final class TestRunContext {
    private static final ThreadLocal<Integer> ITERATION_ID = ThreadLocal.withInitial(() -> 1);

    private TestRunContext() {
    }

    /**
     * Resets the iteration ID for the current thread to 1.
     */
    public static void reset() {
        ITERATION_ID.set(1);
    }

    /**
     * Increments the iteration ID for the current thread by 1.
     */
    public static void incrementIteration() {
        ITERATION_ID.set(getIterationId() + 1);
    }

    /**
     * Returns the current iteration ID for the current thread.
     *
     * @return The current iteration ID.
     */
    public static int getIterationId() {
        return ITERATION_ID.get();
    }

    /**
     * Removes the iteration ID for the current thread.
     */
    public static void clear() {
        ITERATION_ID.remove();
    }
}
