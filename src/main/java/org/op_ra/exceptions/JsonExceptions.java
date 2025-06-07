package org.op_ra.exceptions;

/**
 * Custom exception related to JSON processing errors.
 * This exception can be thrown when issues occur during parsing, generation,
 * or manipulation of JSON data within the framework.
 */
public class JsonExceptions extends FrameworkException {

    /**
     * Constructs a new JsonExceptions with the specified detail message.
     *
     * @param message the detail message.
     */
    public JsonExceptions(String message) {
        super(message);
    }

    /**
     * Constructs a new JsonExceptions with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of the exception (e.g., an underlying Jackson or Gson exception).
     */
    public JsonExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
