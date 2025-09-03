package org.op_ra.exceptions;

/**
 * {@code PropertyFileUsageException} is a runtime exception that is thrown when there is an issue
 * with the usage of a properties file. This exception extends {@link FrameworkException} and is
 * typically thrown when a key or value fetched from a properties file is null, or if the file
 * itself cannot be read.
 *
 * <p>This exception helps in identifying configuration issues early in the test execution cycle.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>This exception can be thrown with a custom message or with a message and a cause.</p>
 *
 * <pre>{@code
 * // Throwing with a custom message
 * if (propertyValue == null) {
 *     throw new PropertyFileUsageException("The value for the specified key is null in the properties file.");
 * }
 *
 * // Throwing with a message and a cause
 * try {
 *     // Attempt to read a properties file
 * } catch (IOException e) {
 *     throw new PropertyFileUsageException("Failed to read the properties file.", e);
 * }
 * }</pre>
 *
 * @see FrameworkException
 * @version 1.0
 * @since 2023-10-27
 */
@SuppressWarnings("serial")
public class PropertyFileUsageException extends FrameworkException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     */
    public PropertyFileUsageException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message The detail message, which is saved for later retrieval by the {@link #getMessage()} method.
     * @param cause The cause of the exception, which is saved for later retrieval by the {@link #getCause()} method.
     *              A {@code null} value is permitted and indicates that the cause is nonexistent or unknown.
     */
    public PropertyFileUsageException(String message, Throwable cause) {
        super(message, cause);
    }

}
