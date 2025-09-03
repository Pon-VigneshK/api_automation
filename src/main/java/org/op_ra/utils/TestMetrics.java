package org.op_ra.utils;

/**
 * {@code TestMetrics} is a final class that holds per-test metrics in a {@link ThreadLocal}
 * variable. This allows the {@link org.op_ra.requestbuilder.RequestBuilder} to record metrics
 * during test execution and the listeners to consume them at the end of the test.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>The methods in this class can be called to set, get, and clear test metrics.</p>
 *
 * <pre>{@code
 * // Setting the response time
 * TestMetrics.setResponseTimeMs(1500L);
 *
 * // Getting the HTTP status code
 * Integer statusCode = TestMetrics.getHttpStatusCode();
 *
 * // Clearing all metrics
 * TestMetrics.clear();
 * }</pre>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public final class TestMetrics {
    private static final ThreadLocal<Long> RESPONSE_TIME_MS = new ThreadLocal<>();
    private static final ThreadLocal<Integer> HTTP_STATUS_CODE = new ThreadLocal<>();
    private static final ThreadLocal<String> ERROR_MESSAGE = new ThreadLocal<>();

    private TestMetrics() {
    }

    /**
     * Clears all the test metrics for the current thread.
     */
    public static void clear() {
        RESPONSE_TIME_MS.remove();
        HTTP_STATUS_CODE.remove();
        ERROR_MESSAGE.remove();
    }

    /**
     * Sets the response time in milliseconds for the current thread.
     *
     * @param ms The response time in milliseconds.
     */
    public static void setResponseTimeMs(long ms) {
        RESPONSE_TIME_MS.set(ms);
    }

    /**
     * Returns the response time in milliseconds for the current thread.
     *
     * @return The response time in milliseconds.
     */
    public static Long getResponseTimeMs() {
        return RESPONSE_TIME_MS.get();
    }

    /**
     * Sets the HTTP status code for the current thread.
     *
     * @param code The HTTP status code.
     */
    public static void setHttpStatusCode(int code) {
        HTTP_STATUS_CODE.set(code);
    }

    /**
     * Returns the HTTP status code for the current thread.
     *
     * @return The HTTP status code.
     */
    public static Integer getHttpStatusCode() {
        return HTTP_STATUS_CODE.get();
    }

    /**
     * Sets the error message for the current thread.
     *
     * @param msg The error message.
     */
    public static void setErrorMessage(String msg) {
        ERROR_MESSAGE.set(msg);
    }

    /**
     * Returns the error message for the current thread.
     *
     * @return The error message.
     */
    public static String getErrorMessage() {
        return ERROR_MESSAGE.get();
    }
}
