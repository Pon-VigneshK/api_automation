package org.op_ra.enums;

/**
 * {@code LogType} is an enumeration that defines the types of log messages for reporting.
 * This enum is used to specify the log level when recording information in the test reports.
 * Using this enum ensures that log levels are consistent and standardized across the framework.
 *
 * <p><b>Usage:</b></p>
 * <p>This enum is used with the {@link org.op_ra.reports.FrameworkLogger} to log messages at different levels.</p>
 *
 * <pre>{@code
 * FrameworkLogger.log(LogType.INFO, "This is an informational message.");
 * FrameworkLogger.log(LogType.ERROR, "An error occurred.");
 * }</pre>
 *
 * <p>The available log types are:</p>
 * <ul>
 *   <li>{@link #INFO} - For informational messages.</li>
 *   <li>{@link #WARN} - For warning messages that indicate a potential issue.</li>
 *   <li>{@link #ERROR} - For error messages that indicate a failure.</li>
 *   <li>{@link #DEBUG} - For debug messages that provide detailed information for troubleshooting.</li>
 *   <li>{@link #TRACE} - For trace messages that provide even more detailed information than debug.</li>
 *   <li>{@link #FATAL} - For fatal errors that cause the application to terminate.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public enum LogType {

    /**
     * A warning message that indicates a potential issue that is not critical.
     */
    WARN,

    /**
     * An error message that indicates a failure or an issue that prevented a specific operation from completing.
     */
    ERROR,

    /**
     * An informational message that provides general guidance or feedback to the user.
     */
    INFO,

    /**
     * A debug message that provides detailed information useful for troubleshooting and development.
     */
    DEBUG,

    /**
     * A trace message that provides highly detailed information, typically used for in-depth debugging.
     */
    TRACE,

    /**
     * A fatal error message that indicates a critical issue that may cause the application to terminate.
     */
    FATAL
}
