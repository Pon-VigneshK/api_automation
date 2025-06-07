package org.op_ra.reports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.op_ra.enums.LogType; // Assuming LogType enum exists

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A custom framework logger that provides a simplified interface over Log4j2
 * and also logs to ExtentReports.
 * <p>
 * This logger allows logging messages with different {@link LogType} levels.
 * Based on the log type, it calls the appropriate methods on both Log4j2 and {@link ExtentLogger}.
 * </p>
 * <p>
 * To use this logger, simply call {@code FrameworkLogger.log(LogType, String)}.
 * </p>
 * Example:
 * <pre>{@code
 * FrameworkLogger.log(LogType.INFO, "This is an informational message.");
 * FrameworkLogger.log(LogType.ERROR, "An error occurred: " + e.getMessage());
 * }</pre>
 */
public final class FrameworkLogger {

    // Get a Log4j2 logger instance for this class.
    private static final Logger LOGGER = LogManager.getLogger(FrameworkLogger.class);

    // Map to associate LogType enums with their corresponding logging actions.
    // This avoids large switch-case or if-else blocks.
    private static final Map<LogType, BiConsumer<String, String>> LOG_ACTIONS_LOG4J = new HashMap<>();
    private static final Map<LogType, BiConsumer<String, String>> LOG_ACTIONS_EXTENT = new HashMap<>();


    static {
        // Initialize Log4j logging actions
        LOG_ACTIONS_LOG4J.put(LogType.DEBUG, (message, details) -> LOGGER.debug(message));
        LOG_ACTIONS_LOG4J.put(LogType.INFO, (message, details) -> LOGGER.info(message));
        LOG_ACTIONS_LOG4J.put(LogType.WARN, (message, details) -> LOGGER.warn(message));
        LOG_ACTIONS_LOG4J.put(LogType.ERROR, (message, details) -> LOGGER.error(message));
        LOG_ACTIONS_LOG4J.put(LogType.FATAL, (message, details) -> LOGGER.fatal(message));
        LOG_ACTIONS_LOG4J.put(LogType.PASS, (message, details) -> LOGGER.info(message)); // Log PASS as INFO in Log4j
        LOG_ACTIONS_LOG4J.put(LogType.FAIL, (message, details) -> LOGGER.error(message)); // Log FAIL as ERROR in Log4j
        LOG_ACTIONS_LOG4J.put(LogType.SKIP, (message, details) -> LOGGER.info(message)); // Log SKIP as INFO in Log4j
        LOG_ACTIONS_LOG4J.put(LogType.REQUEST, (message, details) -> LOGGER.info("REQUEST: " + message + (details != null ? "\n" + details : "")));
        LOG_ACTIONS_LOG4J.put(LogType.RESPONSE, (message, details) -> LOGGER.info("RESPONSE: " + message + (details != null ? "\n" + details : "")));
        LOG_ACTIONS_LOG4J.put(LogType.CONSOLE, (message, details) -> System.out.println(message)); // Special case for console

        // Initialize ExtentReport logging actions
        // Assuming ExtentLogger has methods like pass(), fail(), skip(), info(), warn()
        LOG_ACTIONS_EXTENT.put(LogType.PASS, (message, details) -> ExtentLogger.pass(message + (details != null ? ": " + details : "")));
        LOG_ACTIONS_EXTENT.put(LogType.FAIL, (message, details) -> ExtentLogger.fail(message + (details != null ? ": " + details : "")));
        LOG_ACTIONS_EXTENT.put(LogType.SKIP, (message, details) -> ExtentLogger.skip(message + (details != null ? ": " + details : "")));
        LOG_ACTIONS_EXTENT.put(LogType.INFO, (message, details) -> ExtentLogger.info(message + (details != null ? ": " + details : "")));
        LOG_ACTIONS_EXTENT.put(LogType.WARN, (message, details) -> ExtentLogger.warn(message + (details != null ? ": " + details : "")));
        LOG_ACTIONS_EXTENT.put(LogType.DEBUG, (message, details) -> ExtentLogger.info("DEBUG: " + message + (details != null ? ": " + details : ""))); // Extent might not have specific debug
        LOG_ACTIONS_EXTENT.put(LogType.ERROR, (message, details) -> ExtentLogger.fail("ERROR: " + message + (details != null ? ": " + details : ""))); // Log ERROR as FAIL in Extent
        LOG_ACTIONS_EXTENT.put(LogType.FATAL, (message, details) -> ExtentLogger.fail("FATAL: " + message + (details != null ? ": " + details : ""))); // Log FATAL as FAIL in Extent
        // For REQUEST and RESPONSE, ExtentLogger has specific methods logRequestToReport and logResponseToReport.
        // This generic log method might not be suitable for complex objects like RequestSpecification or Response.
        // Consider calling those methods directly from where requests/responses are handled.
        // However, for simple string messages:
        LOG_ACTIONS_EXTENT.put(LogType.REQUEST, (message, details) -> ExtentLogger.info("REQUEST: " + message + (details != null ? "<br><pre>" + details + "</pre>" : "")));
        LOG_ACTIONS_EXTENT.put(LogType.RESPONSE, (message, details) -> ExtentLogger.info("RESPONSE: " + message + (details != null ? "<br><pre>" + details + "</pre>" : "")));
        LOG_ACTIONS_EXTENT.put(LogType.CONSOLE, (message, details) -> ExtentLogger.info("CONSOLE: " + message)); // Log console output to Extent as INFO
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FrameworkLogger() {
        // Private constructor
    }

    /**
     * Logs a message to both Log4j2 and ExtentReports based on the specified {@link LogType}.
     *
     * @param type    The {@link LogType} of the message (e.g., INFO, ERROR, PASS).
     * @param message The primary message string to log.
     * @param details Optional additional details, which might be formatted differently or logged to specific fields.
     *                Currently, this is appended to the main message for most log types.
     */
    public static void log(LogType type, String message, String details) {
        if (LOG_ACTIONS_LOG4J.containsKey(type)) {
            LOG_ACTIONS_LOG4J.get(type).accept(message, details);
        } else {
            LOGGER.info(message + (details != null ? " | Details: " + details : "")); // Default Log4j action
        }

        if (LOG_ACTIONS_EXTENT.containsKey(type)) {
            LOG_ACTIONS_EXTENT.get(type).accept(message, details);
        } else {
            ExtentLogger.info(message + (details != null ? " | Details: " + details : "")); // Default Extent action
        }
    }

    /**
     * Logs a message to both Log4j2 and ExtentReports based on the specified {@link LogType}.
     * This is an overloaded version of {@link #log(LogType, String, String)} without the details parameter.
     *
     * @param type    The {@link LogType} of the message.
     * @param message The message string to log.
     */
    public static void log(LogType type, String message) {
        log(type, message, null); // Call the main log method with null details
    }
}
