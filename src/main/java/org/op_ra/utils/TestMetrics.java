package org.op_ra.utils;

/**
 * Holds per-test metrics in ThreadLocal so RequestBuilder can record them
 * and listeners can consume them at test end.
 */
public final class TestMetrics {
    private static final ThreadLocal<Long> RESPONSE_TIME_MS = new ThreadLocal<>();
    private static final ThreadLocal<Integer> HTTP_STATUS_CODE = new ThreadLocal<>();
    private static final ThreadLocal<String> ERROR_MESSAGE = new ThreadLocal<>();

    private TestMetrics() {}

    public static void clear() {
        RESPONSE_TIME_MS.remove();
        HTTP_STATUS_CODE.remove();
        ERROR_MESSAGE.remove();
    }

    public static void setResponseTimeMs(long ms) { RESPONSE_TIME_MS.set(ms); }
    public static Long getResponseTimeMs() { return RESPONSE_TIME_MS.get(); }

    public static void setHttpStatusCode(int code) { HTTP_STATUS_CODE.set(code); }
    public static Integer getHttpStatusCode() { return HTTP_STATUS_CODE.get(); }

    public static void setErrorMessage(String msg) { ERROR_MESSAGE.set(msg); }
    public static String getErrorMessage() { return ERROR_MESSAGE.get(); }
}

