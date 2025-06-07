package org.op_ra.enums;

/**
 * Enumeration of log levels or types used within the framework.
 * This can be used by custom logging utilities like {@link org.op_ra.reports.FrameworkLogger}
 * to categorize log messages.
 */
public enum LogType {
    /** For detailed information, typically of interest only when diagnosing problems. */
    DEBUG,
    /** For informational messages that highlight the progress of the application at coarse-grained level. */
    INFO,
    /** For potentially harmful situations or warnings. */
    WARN,
    /** For error events that might still allow the application to continue running. */
    ERROR,
    /** For severe error events that will presumably lead to application termination. */
    FATAL,
    /** For messages related to test case passed status. */
    PASS,
    /** For messages related to test case failed status. */
    FAIL,
    /** For messages related to test case skipped status. */
    SKIP,
    /** For logging API request details. */
    REQUEST,
    /** For logging API response details. */
    RESPONSE,
    /** For console output, if needing a specific type. */
    CONSOLE
}
