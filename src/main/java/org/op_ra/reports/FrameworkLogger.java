package org.op_ra.reports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.op_ra.enums.LogType;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * {@code FrameworkLogger} is a utility class that provides a centralized logging mechanism for the framework.
 * It uses Log4j2 for logging to the console and can be extended to log to other outputs. This class
 * uses a map of consumers to handle different log levels, making it easy to extend and maintain.
 *
 * <p>This class is designed to be used statically and should not be instantiated.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To log a message, call the static {@code log} method with the desired log level and message.</p>
 *
 * <pre>{@code
 * import static org.op_ra.enums.LogType.*;
 * import static org.op_ra.reports.FrameworkLogger.log;
 *
 * log(INFO, "This is an informational message.");
 * log(ERROR, "An error occurred.");
 * }</pre>
 *
 * @see LogType
 * @see Logger
 * @version 1.0
 * @since 2023-10-27
 */
public class FrameworkLogger {
    private static final Logger logger = LogManager.getLogger();
    private static final Consumer<String> TRACE = message -> logger.trace("TRACE: {}", message);
    private static final Consumer<String> WARN = message -> logger.warn("WARN: {}", message);
    private static final Consumer<String> DEBUG = message -> logger.debug("DEBUG: {}", message);
    private static final Consumer<String> ERROR = message -> logger.error("ERROR: {}", message);
    private static final Consumer<String> INFO = message -> logger.info("INFO: {}", message);
    private static final Consumer<String> FATAL = message -> logger.fatal("FATAL: {}", message);
    private static final Map<LogType, Consumer<String>> LOGSMAP = new EnumMap<>(LogType.class);

    static {
        LOGSMAP.put(LogType.TRACE, TRACE);
        LOGSMAP.put(LogType.WARN, WARN);
        LOGSMAP.put(LogType.DEBUG, DEBUG);
        LOGSMAP.put(LogType.ERROR, ERROR);
        LOGSMAP.put(LogType.INFO, INFO);
        LOGSMAP.put(LogType.FATAL, FATAL);
    }

    private FrameworkLogger() {
    }

    /**
     * Logs a message with the specified log level.
     *
     * @param logType The level of the log message, as defined in the {@link LogType} enum.
     * @param message The message to be logged.
     */
    public static void log(LogType logType, String message) {
        Consumer<String> consumer = LOGSMAP.get(logType);
        if (consumer != null) {
            consumer.accept(message);
        } else {
            logger.warn("Unsupported log type: " + logType);
        }
    }
}
