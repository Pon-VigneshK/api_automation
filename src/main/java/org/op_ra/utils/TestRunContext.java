package org.op_ra.utils;

/**
 * Tracks per-test transient context such as iterationId (retries) using ThreadLocal.
 */
public final class TestRunContext {
    private static final ThreadLocal<Integer> ITERATION_ID = ThreadLocal.withInitial(() -> 1);

    private TestRunContext() {}

    public static void reset() { ITERATION_ID.set(1); }
    public static void incrementIteration() { ITERATION_ID.set(getIterationId() + 1); }
    public static int getIterationId() { return ITERATION_ID.get(); }
    public static void clear() { ITERATION_ID.remove(); }
}

