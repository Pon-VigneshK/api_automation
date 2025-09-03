package org.op_ra.exceptions;

/**
 * {@code JsonExceptions} is a runtime exception that is thrown when there is an error
 * related to JSON processing. This exception extends {@link FrameworkException} and is used
 * to indicate issues such as parsing errors, serialization problems, or other JSON-related failures.
 *
 * <p>This exception is typically thrown when working with JSON data, such as when reading a
 * JSON file, converting a Java object to a JSON string, or vice versa.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>This exception can be thrown with a custom message or with a message and a cause.</p>
 *
 * <pre>{@code
 * // Throwing with a custom message
 * try {
 *     // Some JSON operation
 * } catch (JsonProcessingException e) {
 *     throw new JsonExceptions("Failed to process the JSON data.", e);
 * }
 *
 * // Throwing with a message and a cause
 * try {
 *     // Another JSON operation
 * } catch (IOException e) {
 *     throw new JsonExceptions("An I/O error occurred while processing the JSON.", e);
 * }
 * }</pre>
 *
 * @see FrameworkException
 * @version 1.0
 * @since 2023-10-27
 */
@SuppressWarnings("serial")
public class JsonExceptions extends FrameworkException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public JsonExceptions(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause The cause of the exception, which is saved for later retrieval by the {@link #getCause()} method.
     *              A {@code null} value is permitted and indicates that the cause is nonexistent or unknown.
     */
    public JsonExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}